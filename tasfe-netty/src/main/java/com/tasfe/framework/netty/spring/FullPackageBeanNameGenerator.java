package com.tasfe.framework.netty.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

public class FullPackageBeanNameGenerator extends AnnotationBeanNameGenerator {
    public static final char DEFAULT_DOT_REPLACEMENT = '_';
    private int packageDepth = 1;
    private char dotReplacement = DEFAULT_DOT_REPLACEMENT;

    public int getPackageDepth() {
        return packageDepth;
    }

    public void setPackageDepth(int packageDepth) {
        this.packageDepth = packageDepth;
    }

    public char getDotReplacement() {
        return dotReplacement;
    }

    public void setDotReplacement(char dotReplacement) {
        this.dotReplacement = dotReplacement;
    }

    @Override
    protected String buildDefaultBeanName(BeanDefinition definition) {
        if (this.packageDepth < 1) {
            return super.buildDefaultBeanName(definition);
        }

        String fullName = definition.getBeanClassName(); //e.g.: com.tasfe.tkj.api.action.UserAction
        char delimiter = dotReplacement > 0 ? dotReplacement : DEFAULT_DOT_REPLACEMENT;
        String[] names = fullName.split("\\.");
        StringBuilder beanName = new StringBuilder("");

        //add package name
        if (names.length > 1) {
            for (int i = 0; i < this.packageDepth; i++) {
                String name = names[names.length - 2 - i];
                beanName.insert(0, delimiter);
                beanName.insert(0, name);
            }
        }

        //add Class name
        beanName.append(names[names.length - 1]);

        return beanName.toString();
    }
}
