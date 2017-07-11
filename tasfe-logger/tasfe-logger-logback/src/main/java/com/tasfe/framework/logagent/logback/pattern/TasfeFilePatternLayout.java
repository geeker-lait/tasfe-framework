package com.tasfe.framework.logagent.logback.pattern;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

/**
 * Created by lait on 2017/6/7.
 */
public class TasfeFilePatternLayout<E> extends PatternLayoutEncoderBase<E> {

    public TasfeFilePatternLayout() {
    }

    public void start() {
        PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(this.context);
        patternLayout.setPattern(this.getPattern());
        patternLayout.setOutputPatternAsHeader(this.outputPatternAsHeader);
        patternLayout.start();
        super.start();
    }

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }
}
