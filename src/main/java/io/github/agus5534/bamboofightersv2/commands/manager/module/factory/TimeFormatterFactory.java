package io.github.agus5534.bamboofightersv2.commands.manager.module.factory;

import io.github.agus5534.bamboofightersv2.commands.manager.module.parts.TimeFormatterPart;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class TimeFormatterFactory implements PartFactory {
    @Override
    public CommandPart createPart(String s, List<? extends Annotation> list) {
        return new TimeFormatterPart(s);
    }
}
