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

/**
 * Sample layered icon abstraction.
 */
public interface LayeredIcon extends Icon {

    /**
     * Creates a new layered icon instance with the given icons.
     *
     * @param icons the icons to include
     * @return the newly created {@link LayeredIcon}
     */
    static LayeredIcon create(Icon... icons) {
        return new LayeredIconImpl(icons);
    }

    /**
     * Creates a new {@link LayeredIcon} instance with the given amount of levels.
     *
     * @param depth the amount of levels to create
     * @return the created {@code LayeredIcon}
     */
    static LayeredIcon create(int depth) {
        return new LayeredIconImpl(depth);
    }

    /**
     * Returns all stored icon layers.
     *
     * @return all stored icon layers.
     */
    Icon[] getIconLayers();

    /**
     * Returns the icon from the given layer. Note that the returned value
     * may be {@code null} if no {@link Icon} has been set for the layer.
     *
     * @param layer the icon layer
     * @return the icon at the specified layer
     */
    Icon getIcon(int layer);

    /**
     * Returns the icon for the dark look and feel.
     *
     * @param isDarkLaf whether the dark LaF is set
     * @return the default icon for the dark layout
     */
    Icon getIcon(boolean isDarkLaf);

    /**
     * Returns the amount of layers this icon have.
     *
     * @return layer amount
     */
    int getLayerCount();

    /**
     * Sets the given {@link Icon} at the specified layer.
     *
     * @param icon  the icon to set
     * @param layer the layer where the icon will be placed
     */
    void setIcon(Icon icon, int layer);

    /**
     * Returns whether the given layer is enabled and will be painted.
     *
     * @param layer the layer to inspect
     * @return {@code true} if the layer will be painted
     */
    boolean isEnabled(int layer);

    /**
     * Enabled or disables the given layer.
     *
     * @param layer   the layer to disable/enable
     * @param enabled whether the layer should be enabled/disabled
     */
    void setEnabled(int layer, boolean enabled);

    /**
     * Draw the icon at the specified location.  Icon implementations
     * may use the Component argument to get properties useful for
     * painting, e.g. the foreground or background color.
     * <p>
     * Layered icons should be painted as follows:
     * <pre>
     *     {@link Icon}[] icons = getIconLayers();
     *     for (int i = 0; i &lt; icons.length; i++) {
     *         {@link Icon} icon = icons[i];
     *         if (icon == null || !isEnabled(i)) continue;
     *         icon.paintIcon(c, g, x, y);
     *     }
     * </pre>
     *
     * @param c a {@code Component} to get properties useful for painting
     * @param g the graphics context
     * @param x the X coordinate of the icon's top-left corner
     * @param y the Y coordinate of the icon's top-left corner
     */
    @Override
    void paintIcon(Component c, Graphics g, int x, int y);

    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    @Override
    int getIconWidth();

    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    @Override
    int getIconHeight();
}
