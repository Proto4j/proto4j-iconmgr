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
import java.util.Arrays;
import java.util.Objects;

class LayeredIconImpl implements LayeredIcon {

    private final Icon[]    layers;
    private final boolean[] layerStates;

    volatile int width;
    volatile int height;

    public LayeredIconImpl(Icon... icons) {
        layers      = icons;
        layerStates = new boolean[icons.length];
        Arrays.fill(layerStates, true);
        width  = computeWidth();
        height = computeHeight();
    }

    public LayeredIconImpl(int size) {
        layers      = new Icon[size];
        layerStates = new boolean[size];
    }

    @Override
    public Icon getIcon(boolean isDarkLaf) {
        LayeredIcon icon = new LayeredIconImpl(layers);
        for (int i = 0; i < icon.getLayerCount(); i++) {
            Icon iconLayer = icon.getIcon(i);

            if (iconLayer instanceof ImageDescriptor) {
                ImageDescriptor id = (ImageDescriptor) iconLayer;

                if ((id.getFlags() & ImageDescriptor.HAS_DARK) != 0) {
                    iconLayer = id.getDarkIcon();
                    if (iconLayer != null) {
                        icon.setIcon(iconLayer, i);
                    }
                }
            }
        }
        return icon;
    }

    @Override
    public Icon [] getIconLayers() {
        return layers;
    }

    @Override
    public Icon getIcon(int layer) {
        return layers[layer];
    }

    @Override
    public int getLayerCount() {
        return layers.length;
    }

    @Override
    public void setIcon(Icon icon, int layer) {
        layers[layer] = icon;
        if (icon.getIconHeight() > height) {
            height = icon.getIconHeight();
        }
        if (icon.getIconWidth() > width) {
            width = icon.getIconWidth();
        }
        setEnabled(layer, true);
    }

    @Override
    public boolean isEnabled(int layer) {
        return layerStates[layer];
    }

    @Override
    public void setEnabled(int layer, boolean enabled) {
        layerStates[layer] = enabled;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Icon[] icons = getIconLayers();
        for (int i = 0; i < icons.length; i++) {
            Icon icon = icons[i];
            if (icon == null || !isEnabled(i)) continue;
            icon.paintIcon(c, g, x, y);
        }
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    private int computeHeight() {
        return Arrays.stream(layers).filter(Objects::nonNull)
                     .mapToInt(Icon::getIconHeight)
                     .max().orElse(0);
    }

    private int computeWidth() {
        return Arrays.stream(layers).filter(Objects::nonNull)
                     .mapToInt(Icon::getIconWidth)
                     .max().orElse(0);
    }
}
