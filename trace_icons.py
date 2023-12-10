import argparse
import dataclasses
import io
import pathlib


@dataclasses.dataclass(frozen=False)
class JField:
    """
    Represents an icon field in the generated Java interface.
    """
    name: str
    key: int
    flags: int
    path: str

    def __str__(self):
        """
       Returns a formatted string representing the icon field.

       Returns:
           str: Formatted string representation of the icon field.
       """
        # Icon <name> = load("<path>", HEX(<key>), HEX(<flags>));
        return f"public static final Icon {self.name.capitalize()} = load(\"/{self.path}\", {hex(self.key)}L, {hex(self.flags)});"


def get_flags(name: str) -> int:
    """
    Returns the flags associated with an icon based on its name.

    Args:
        name (str): The name of the icon.

    Returns:
        int: Flags associated with the icon.
    """
    if '@2x.' in name:
        return 1
    elif '@2x_dark' in name:
        return 4
    elif '_dark' in name:
        return 2
    elif '_stroke' in name:
        return 8
    else:
        return 0


def normalize_name(name: str) -> str:
    """
    Normalizes the name of an icon by removing specific targets from it.

    Args:
        name (str): The original name of the icon.

    Returns:
        str: The normalized name.
    """
    result = name
    for target in ("_dark", "_stroke", "@2x"):
        result = result.replace(target, "")
    return result.replace("-", "_")


HEAD = """// GENERATED FILE - DO NOT MODIFY
package %s;

import javax.swing.*;
import io.github.proto4j.ui.icon.*;

public final class %s {
    private static final IconManager manager = SVGIconManager.getInstance(%s.class);\n\n
"""

FOOTER = """
    private static Icon load(String path, long key, int flags) {
            return manager.loadIcon(path, key, flags);
    }
}"""


def get_header(package: str, name: str) -> str:
    """
    Returns the header of the generated Java interface.

    Args:
        package (str): The Java package name.
        name (str): The name of the interface.

    Returns:
        str: The formatted header string.
    """
    return HEAD % (package, name, name)


class IconCollector:
    """
    Collects and processes icon files to generate the Java interface.
    """

    def __init__(self, key: int = -243615036, src: str = None, extensions=None):
        """
        Initializes the IconCollector.

        Args:
            key (int, optional): The starting key for icons. Defaults to -24361507436.
            src (str, optional): The source directory path. Defaults to None.
            extensions (list[str], optional): The list of file extensions to consider as icons. Defaults to None.
        """
        self.icon_map = {}
        self.key = key
        self.src = src or "."
        self.extensions = extensions or [".svg", ".png"]

    def process(self, directory: pathlib.Path):
        """
        Processes icons in the specified directory and its subdirectories.

        Args:
            directory (pathlib.Path): The directory path containing icon files.
        """
        self._process_dir(directory, self.icon_map)

    def _process_dir(self, path: pathlib.Path, imap: dict):
        for child in path.iterdir():
            if not child.is_dir():
                self.key -= 1
                self._process_file(child, imap)
            else:
                sub_map = {}
                self._process_dir(child, sub_map)
                imap[child.name] = sub_map

    def _process_file(self, path: pathlib.Path, dest: dict):
        if path.suffix in self.extensions:
            flags = get_flags(path.name)
            name = normalize_name(path.stem)
            full_name = path.parent / name

            idx = str(full_name).find(self.src)
            if idx != -1:
                full_name = full_name.as_posix()[idx:]
            else:
                full_name = full_name.as_posix()

            for icon_path in dest:
                jfield = dest[icon_path]
                if type(jfield) == JField and jfield.name == name:
                    jfield.flags |= flags
                    return

            dest[full_name] = JField(name, self.key, flags, full_name)


def write_class(fp: io.IOBase, path: pathlib.Path, values: dict):
    """
    Writes the icon class to the output file.

    Args:
        fp (io.IOBase): The output file object.
        path (pathlib.Path): The path of the icon class.
        values (dict): The dictionary containing the icon fields.
    """
    fp.write(f"public static final class {path.name.capitalize()} {{\n")

    for icon_path in values:
        value = values[icon_path]
        if isinstance(value, JField):
            fp.write(str(value) + "\n")
        else:
            write_class(fp, pathlib.Path(icon_path), value)

    fp.write("}\n\n")
    fp.flush()


def write(fp: io.IOBase, package: str, name: str, values: dict):
    """
    Writes the generated Java interface to the output file.

    Args:
        fp (io.IOBase): The output file object.
        package (str): The Java package name.
        name (str): The name of the interface.
        values (dict): The dictionary containing the icon fields.
    """
    fp.write(get_header(package, name))
    for icon_path in values:
        value = values[icon_path]
        if isinstance(value, JField):
            fp.write(str(value) + "\n")
        else:
            write_class(fp, pathlib.Path(icon_path), value)
    fp.write(FOOTER)
    fp.flush()


if __name__ == '__main__':
    parser = argparse.ArgumentParser(epilog="""
    Example > trace_icons.py /path/to/icons --src icons -p "com.example"
    """)
    parser.add_argument('path', default='./', help="The path to the directory containing icon files.")

    parser.add_argument('-o', '--out', default='./AllIcons.java', help="The output file path.")
    parser.add_argument('-n', '--name', default="AllIcons", help="The name of the generated Java interface.")
    parser.add_argument('-p', '--package', default="java", help="The Java package name.")
    parser.add_argument('-s', '--src', required=True, help="The source directory name containing icon files.")

    argv = parser.parse_args()

    collector = IconCollector(src=argv.src)
    collector.process(pathlib.Path(argv.path))
    with open(argv.out, "w", encoding="utf-8") as fp:
        write(fp, argv.package, argv.name, collector.icon_map)
