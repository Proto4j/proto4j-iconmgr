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

package io.github.proto4j.ui.icon;

import io.github.proto4j.ui.SVGUtil;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
/**
 * The SVGIconManager class is responsible for managing SVG icons and loading image resources.
 * It implements the IconManager interface, which provides methods for handling icons.
 */
public final class SVGIconManager implements IconManager {

    // Singleton instance of the SVGIconManager
    private static final SVGIconManager instance = new SVGIconManager(SVGIconManager.class);

    // The target class for which the SVGIconManager is created
    private final Class<?> targetClass;

    /**
     * Private constructor for SVGIconManager.
     * It is called internally to create a new SVGIconManager instance.
     *
     * @param targetClass The class for which this SVGIconManager is created.
     */
    private SVGIconManager(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Get the singleton instance of SVGIconManager for a specific target class.
     *
     * @param targetClass The class for which to get the SVGIconManager instance.
     * @return The SVGIconManager instance.
     */
    public static SVGIconManager getInstance(Class<?> targetClass) {
        return new SVGIconManager(targetClass);
    }

    /**
     * Get the global singleton instance of SVGIconManager.
     *
     * @return The global SVGIconManager instance.
     */
    public static SVGIconManager getGlobal() {
        return instance;
    }

    /**
     * Get the target class associated with this SVGIconManager.
     *
     * @return The target class associated with the SVGIconManager.
     */
    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    /**
     * Load an icon from the specified path using the provided ClassLoader and key.
     *
     * @param path        The path to the icon resource.
     * @param classLoader The ClassLoader to load the icon resource.
     * @param key         The key for the icon resource.
     * @param flags       Additional flags for loading the icon.
     * @return An instance of ImageDescriptor representing the loaded icon.
     */
    @Override
    public Icon loadIcon(String path, ClassLoader classLoader, long key, int flags) {
        return new ImageDescriptor(this, path, flags, key);
    }

    /**
     * Get an ImageIcon from the specified path.
     * <p>
     * If the path ends with ".svg", it loads the SVG resource; otherwise, it loads a regular image resource.
     *
     * @param path The path to the image resource, including the filename and extension.
     * @return An instance of ImageIcon representing the loaded image.
     * @throws RuntimeException If the image resource cannot be resolved or an I/O error occurs.
     */
    @Override
    public ImageIcon getImageIcon(String path) {
        try {
            URL stream = getTargetClass().getResource(path);
            if (stream == null) {
                throw new RuntimeException(path + " not resolved");
            }
            if (path.endsWith(".svg")) {
                return new ImageIcon(SVGUtil.loadSVG(stream));
            } else {
                return new ImageIcon(stream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
