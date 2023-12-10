# Icon-Manager (_iconmgr_)

A small Java library that simplifies the usage of icons in your application.

## Integration

You have two options: you can either download the packed JAR from the releases tab or use a packaging system like Maven or Gradle. For Maven integration, it would look like this:

```xml
<dependency>
  <groupId>io.github.proto4j</groupId>
  <artifactId>proto4j-iconmgr</artifactId>
  <version>XXX</version>
</dependency>
```

## How To

Instances of an `IconManager` are both easy to use and easy to implement. The default implementation comes with enhanced SVG support:

```java
class Example {
    public static void main(String[] args) {
        IconManager manager = SVGIconManager.getInstance(Example.class);
        // Sample icon with no LaF support (image can be SVG or PNG)
        ImageDescriptor icon = (ImageDescriptor) manager.loadIcon("/icons/example", 0x1234);
    }
}
```

To reduce the number of times you have to reference a new icon manager instance, you can use the Python script delivered with this repository to generate a Java class that stores all the icons you need. An example output class would look like this:

```java
public final class AllIcons {
    private static final IconManager manager = SVGIconManager.getInstance(AllIcons.class);
    // Icons in root directory
    public static final Icon test = load("/icons/test", -0x5ac0f1a6dL, 0x0);
    
    public static final class Directory {
        public static final Icon test = load("/icons/directory/test2", -0x5ac0f1a6eL, 0x0);
    }
    // ... (method load(...) will be generated into this class)
}
```

The Java file created can be copied into your project, and all fields can be referenced directly. Icons can be named using the following conventions:

    example.svg: The standard version of the icon.
    example_dark.svg: A version of the icon specifically designed for dark mode.
    example@2x.svg: A larger version of the standard icon (2x size).
    example@2x_dark.svg: A larger version of the dark mode icon (2x size).
    example_stroke.svg: An image of the icon with a stroke.

## License

This project is licensed under the MIT License.
