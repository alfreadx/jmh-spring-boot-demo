package com.github.alfreadx.jmhspringdemo.jmh;

import com.github.alfreadx.jmhspringdemo.JmhSpringDemoApplication;
import com.github.alfreadx.jmhspringdemo.model.UserCreateIO;
import com.github.alfreadx.jmhspringdemo.service.UserService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class SpringJmhTest {

    UserService userSvc;
    UserCreateIO createIO;
    private ConfigurableApplicationContext context;

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(SpringJmhTest.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .resultFormat(ResultFormatType.JSON)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .verbosity(VerboseMode.EXTRA)
                .jvmArgs("-Xmx5136m")
                .build();

        new Runner(options).run();
    }

    @Setup
    public void init() {
        context = SpringApplication.run(JmhSpringDemoApplication.class);
        userSvc = context.getBean(UserService.class);

        createIO = UserCreateIO.builder()
                .account("foo")
                .password("hahahhaha")
                .build();
    }

    @Benchmark
    public void createUser1() {
        userSvc.createUser2(createIO);
    }

    @Benchmark
    public void createUser2() {

        userSvc.createUser(createIO);
    }


    @TearDown
    public void tearDown() throws InterruptedException {
        context.close();


        // 有時候還是會發生 "JMH had finished, but forked VM did not exit, are there stray running threads?" 的問題，就加上下面這段
//        while (context.isRunning()) {
//            System.out.println("round 2 close");
//            context.close();
//            Thread.sleep(100);
//        }
    }
}
