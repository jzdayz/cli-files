package io.github.jzdayz;

import org.springframework.util.AntPathMatcher;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "checksum", mixinStandardHelpOptions = true, version = "checksum 4.0",
         description = "Prints the checksum (MD5 by default) of a file to STDOUT.")
class Files implements Callable<Integer> {

    @Parameters(index = "0", description = "The file whose checksum to calculate.")
    private File file;

    @Option(names = {"-n", "--name"}, description = "ant")
    private String ant = "*";

    private AntPathMatcher matcher = new AntPathMatcher();

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new Files()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        show(file);
        return 0;
    }

    public void action(Do<?> consumer){
        try {
            consumer.doSomething();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    interface Do<T>{
        void doSomething() throws Exception;
    }

    private long show(File file) throws Exception{
        if (file.isDirectory()){
            java.nio.file.Files.list(file.toPath()).forEach(path->{
                action(()->show(path.toFile()));
            });
        }
        if (matcher.match(ant,file.getName())) {
            System.out.println(file);
        }
        return file.length();
    }

}