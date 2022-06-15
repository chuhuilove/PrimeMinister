package com.chuhui.primeminister.collector.config;

import com.chuhui.primeminister.collector.annotations.PrimeMinisterConfiguration;

import java.util.List;

/**
 * NetworkConfig
 *
 * @author: cyzi
 * @Date: 6/13/22
 * @Description:
 */

@PrimeMinisterConfiguration(prefix = "filter")
public class FilterConfig {

    private List<String> forbidTransformedClassNames;
    private List<String> forbidTransformedPackageNames;

    public List<String> getForbidTransformedClassNames() {
        return forbidTransformedClassNames;
    }

    public void setForbidTransformedClassNames(List<String> forbidTransformedClassNames) {
        this.forbidTransformedClassNames = forbidTransformedClassNames;
    }

    public List<String> getForbidTransformedPackageNames() {
        return forbidTransformedPackageNames;
    }

    public void setForbidTransformedPackageNames(List<String> forbidTransformedPackageNames) {
        this.forbidTransformedPackageNames = forbidTransformedPackageNames;
    }

    public boolean hasFilteredPackageName(String className) {
        for (String packageName : forbidTransformedClassNames) {
            if (className.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }
}
