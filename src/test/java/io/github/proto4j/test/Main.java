package io.github.proto4j.test;

import io.github.proto4j.ui.icon.ImageDescriptor;

public class Main {
    public static void main(String[] args) {
        ImageDescriptor icon = (ImageDescriptor) AllIcons.Test;

        System.out.println(icon.getDefaultIcon());

    }
}