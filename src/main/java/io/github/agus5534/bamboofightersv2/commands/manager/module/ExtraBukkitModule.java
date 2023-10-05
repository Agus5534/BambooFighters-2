package io.github.agus5534.bamboofightersv2.commands.manager.module;

import io.github.agus5534.agusutils.annotations.arguments.Factory;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import org.bukkit.Bukkit;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class ExtraBukkitModule extends AbstractModule {
    private final HashMap<Type, PartFactory> partFactorySet;
    public ExtraBukkitModule() {
        partFactorySet = new HashMap<>();

        var classes = getClasses();
        Bukkit.getLogger().info("Found Classes: " + classes.size());



        classes.forEach(c -> {
            var a = c.getAnnotation(Factory.class);

            try {
                var ins = (PartFactory) Class.forName(c.getPackageName() + ".factories." + c.getSimpleName() + "Factory").newInstance();

                Bukkit.getLogger().info(String.format("Binded %s as instance %s", a.c(), ins.getClass().getName()));

                partFactorySet.put(a.c(), ins);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void configure() {
        partFactorySet.forEach(this::bindFactory);
    }

    private Set<Class<?>> getClasses() {
        var reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackage("io.github.agus5534.bamboofightersv2.commands.manager.module.parts")
                        .setScanners(TypesAnnotated)
        );


        return reflections.get(TypesAnnotated.with(Factory.class).asClass());
    }
}
