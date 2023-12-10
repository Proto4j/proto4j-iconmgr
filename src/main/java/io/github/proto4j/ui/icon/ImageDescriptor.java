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

import javax.swing.*;
import java.awt.*;

public final class ImageDescriptor extends ImageIcon implements Icon {
    // Constants for flag values
    public static final int HAS_2x = 0x1;
    public static final int HAS_DARK = 0x2;
    public static final int HAS_DARK_2x = 0x4;
    public static final int HAS_STROKE = 0x8;

    // Instance variables
    final long id;
    final int flags;
    final String path;
    private final Icon[] icons = {
            null, // dark
            null, // dark2x
            null, // 2x
            null  // stroke
    };
    private Icon defaultIcon;

    // Constructor
    public ImageDescriptor(IconManager manager, String path, int flags, long id) {
        // Initialize instance variables
        this.flags = flags;
        this.id = id;
        this.path = path;

        // Load the image variations using the provided IconManager
        loadImages(manager, path);
    }

    // Private helper method to load different image variations
    private void loadImages(IconManager manager, String path) {
        // Transform the resource path using the IconManager
        String transformed = manager.transformResourcePath(path);

        // Load the defaultIcon (base icon)
        defaultIcon = loadImage(manager, transformed + ".svg");

        // Load additional image variations based on flags
        if ((flags & HAS_DARK) != 0) {
            icons[0] = loadImage(manager, transformed + "_dark.svg");
        }
        if ((flags & HAS_DARK_2x) != 0) {
            icons[1] = loadImage(manager, transformed + "@2x_dark.svg");
        }
        if ((flags & HAS_2x) != 0) {
            icons[2] = loadImage(manager, transformed + "@2x.svg");
        }
        if ((flags & HAS_STROKE) != 0) {
            icons[3] = loadImage(manager, transformed + "_stroke.svg");
        }
    }

    // Private helper method to load an image using the IconManager
    private Icon loadImage(IconManager manager, String transformed) throws RuntimeException {
        try {
            return manager.getImageIcon(transformed);
        } catch (RuntimeException e) {
            // If loading the SVG icon fails, fallback to loading a PNG icon
            return manager.getImageIcon(transformed.replace(".svg", ".png"));
        }
    }

    // Icon interface method to draw the icon at the specified location
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        // If the look and feel is dark and there is a dark icon available, use it
        if (isLafDark() && getDarkIcon() != null) {
            getDarkIcon().paintIcon(c, g, x, y);
        } else {
            // Otherwise, use the default icon
            defaultIcon.paintIcon(c, g, x, y);
        }
    }

    // Getter method for flags
    public int getFlags() {
        return flags;
    }

    // Icon interface method to get the icon's width
    @Override
    public int getIconWidth() {
        return defaultIcon.getIconWidth();
    }

    // Icon interface method to get the icon's height
    @Override
    public int getIconHeight() {
        return defaultIcon.getIconHeight();
    }

    // Getter methods for different image variations
    public Icon getDarkIcon() {
        return icons[0];
    }

    public Icon getDark2xIcon() {
        return icons[1];
    }

    public Icon get2xIcon() {
        return icons[2];
    }

    public Icon getStrokeIcon() {
        return icons[3];
    }

    public Icon getDefaultIcon() {
        return defaultIcon;
    }

    // ImageIcon method to get the image
    @Override
    public Image getImage() {
        // If the look and feel is dark and there is a dark icon available, use its image
        if (isLafDark() && getDarkIcon() != null) {
            return ((ImageIcon) getDarkIcon()).getImage();
        } else {
            // Otherwise, use the default icon's image
            return ((ImageIcon) getDefaultIcon()).getImage();
        }
    }

    // Custom method to get the unique key associated with the image descriptor
    public long key() {
        return id;
    }

    // Custom method to get the path of the image descriptor
    public String getPath() {
        return path;
    }

    // Private helper method to check if the look and feel is dark
    private boolean isLafDark() {
        try {
            // Checking whether the current look and feel is dark by executing the FlatLaf#isLafDark() method.
            Class<?> cls = Class.forName("com.formdev.flatlaf.FlatLaf");
            return (Boolean) cls.getMethod("isLafDark").invoke(null);
        } catch (Exception e) {
            // If an exception occurs (e.g., FlatLaf class not found), assume the look and feel is not dark.
            return false;
        }
    }
}
