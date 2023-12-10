/*
* MIT License
*
* Copyright (c) 2023 Proto4j
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

package io.github.proto4j.ui.icon;//@date 29.07.2023

import javax.swing.*;

/**
 * All manager implementations should extend this interface. It provides methods
 * for loading and managing icons and image icons.
 * <p>
 * Each loaded icon (not {@link ImageIcon}) stores the following attributes:
 * <ol>
 *     <li><b>key</b>: the identification ID for this icon</li>
 *     <li><b>flags</b>: custom flags that indicate presence of additional icons</li>
 *     <li><b>path</b>: relative path to the resource file</li>
 * </ol>
 *
 * @author MatrixEditor
 */
public interface IconManager {

    /**
     * Get the target class associated with the IconManager.
     *
     * @return The target class.
     */
    Class<?> getTargetClass();

    /**
     * Load an Icon using the provided path and key with default flags.
     *
     * @param path The path to the icon.
     * @param key  The key associated with the icon.
     * @return The loaded Icon.
     */
    default Icon loadIcon(String path, long key) {
        return loadIcon(path, key, 0);
    }

    /**
     * Load an Icon using the provided path, key, and flags.
     *
     * @param path  The path to the icon.
     * @param key   The key associated with the icon.
     * @param flags Flags for loading the icon.
     * @return The loaded Icon.
     */
    default Icon loadIcon(String path, long key, int flags) {
        return loadIcon(path, getTargetClass(), key, flags);
    }

    /**
     * Load an Icon using the provided path, class, key, and flags.
     *
     * @param path  The path to the icon.
     * @param cls   The class associated with the icon.
     * @param key   The key associated with the icon.
     * @param flags Flags for loading the icon.
     * @return The loaded Icon.
     */
    default Icon loadIcon(String path, Class<?> cls, long key, int flags) {
        return loadIcon(path, cls.getClassLoader(), key, flags);
    }

    /**
     * Load an Icon using the provided path, class loader, key, and flags.
     *
     * @param path        The path to the icon.
     * @param classLoader The ClassLoader to load the icon resource.
     * @param key         The key associated with the icon.
     * @param flags       Flags for loading the icon.
     * @return The loaded Icon.
     */
    Icon loadIcon(String path, ClassLoader classLoader, long key, int flags);

    /**
     * Get an ImageIcon using the provided path.
     *
     * @param path The path to the image icon.
     * @return The loaded ImageIcon.
     */
    ImageIcon getImageIcon(String path);

    /**
     * Transform the resource path if needed.
     *
     * @param path The original resource path.
     * @return The transformed resource path.
     */
    default String transformResourcePath(String path) {
        return path;
    }
}
