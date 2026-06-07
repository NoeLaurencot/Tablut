import boardifier.model.action.ActionList;

import java.lang.management.ManagementFactory;
import java.util.function.Supplier;

import com.sun.management.ThreadMXBean;

import static org.mockito.Mockito.mock;

public class Profiler {
    public static long timespan;

    /**
     * Function to analyze the decide function to get data about how much time it took and how much memory.
     * @param actionListFunction
     * @return
     */
    public static ActionList analyze(Supplier<ActionList> actionListFunction) {
        ThreadMXBean threadMXBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        long memoryUsed = threadMXBean.getThreadAllocatedBytes(Thread.currentThread().threadId());
        timespan = timestamp();
        ActionList rst = actionListFunction.get();
        timespan = timestamp() - timespan;
        memoryUsed = threadMXBean.getThreadAllocatedBytes(Thread.currentThread().threadId()) - memoryUsed;
        System.out.println("Memory used : " + memoryUsed);
        System.out.println("Timespan : " + timespan);
        return rst;
    }

    /**
     * return current time in ns.
     * @return
     */
    public static long timestamp() {
        return System.nanoTime();
    }
}
