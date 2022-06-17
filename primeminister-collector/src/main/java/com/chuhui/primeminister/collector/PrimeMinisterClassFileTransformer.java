package com.chuhui.primeminister.collector;

import com.chuhui.primeminister.collector.config.FilterConfig;
import com.chuhui.primeminister.collector.config.PrimeMinisterConfig;
import com.chuhui.primeminister.collector.network.PrimeMinisterNetworkClient;
import com.chuhui.primeminister.collector.plugin.PrimeMinisterPluginSpecification;
import com.chuhui.primeminister.collector.utils.CollectionsUtils;
import com.chuhui.primeminister.collector.utils.StatisticsLoadedClassCyzi;
import com.chuhui.primeminister.collector.utils.StringUtils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PrimeMinisterClassFileTransformer
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public class PrimeMinisterClassFileTransformer implements ClassFileTransformer {

    private final static byte[] NO_TRANSFORMED = null;

    private final List<PrimeMinisterPluginSpecification> supportedPluginSpecifications;
    private final PrimeMinisterNetworkClient networkClient;

    private final Set<String> forbidTransformedClassNames = new HashSet<>();
    private final Set<String> forbidTransformedPackageNames = new HashSet<>();

    public PrimeMinisterClassFileTransformer(List<PrimeMinisterPluginSpecification> supportedPlugins) {

        supportedPluginSpecifications = new ArrayList<>(supportedPlugins.size());
        supportedPluginSpecifications.addAll(supportedPlugins);
//        forbidTransformedClassNames.add(null);
//        forbidTransformedPackageNames.add("com.chuhui.primeminister");
      //  buildFilter();
        networkClient = new PrimeMinisterNetworkClient();
    }


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (StringUtils.isEmpty(className)) {
            return NO_TRANSFORMED;
        }

        // 如果loader为空,则表明是引导类加载器加载的类
        StatisticsLoadedClassCyzi.putLoadedClass(loader, className);


        if (CollectionsUtils.isNotEmpty(supportedPluginSpecifications)) {
            ClassDescInfoHolder classHolder = new ClassDescInfoHolder(className, loader, classBeingRedefined, classfileBuffer,networkClient);

//            boolean isForbidTransformed = forbidTransformedClassNames.contains(classHolder.getClassName())
//                    || PrimeMinisterConfig.getConfigInstance().getFilterConfig().hasFilteredPackageName(classHolder.getClassName());
//
//            if (isForbidTransformed) {
//                return NO_TRANSFORMED;
//            }


            for (PrimeMinisterPluginSpecification plugin : supportedPluginSpecifications) {
                if (plugin.isSupported(classHolder)) {
                    // 一个class只允许织入一次
                    System.err.println("本次允许的类:" + classHolder);

                    return plugin.customizationByteCode(classHolder);
                }
            }

        }
        return NO_TRANSFORMED;
    }


    private void buildFilter() {
        PrimeMinisterConfig configInstance = PrimeMinisterConfig.getConfigInstance();
        FilterConfig filterConfig = configInstance.getFilterConfig();
        forbidTransformedClassNames.addAll(filterConfig.getForbidTransformedClassNames());
        forbidTransformedPackageNames.addAll(filterConfig.getForbidTransformedPackageNames());
    }
}
