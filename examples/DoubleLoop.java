import net.devmentality.jprofile.JProfile;

/**
 * This is an example of an O(N^2) algorithm being profiled by JProfile.
 * For a more detailed explanation of how JProfile works, see FillList.java.
 */
public class DoubleLoop {
    private static int doubleLoop(int num){
        int n = 0;
        for(int i = 0; i < num; i++){
            for(int j = 0; j < num; j++){
                n += i + j;
            }
        }

        return n;
    }

    public static void main(String[] args){
        int n_min = 1;
        int n_max = 10000;
        int n_step = 100;

        int loops_per_sample = 100;

        JProfile<Integer> p = new JProfile<>("Double Loop", 1000);

        for(int num = n_min; num <= n_max; num += n_step){
            p.startSample(num);

            int result = doubleLoop(num);

            p.stopSample(num, loops_per_sample);
        }

        p.export(JProfile.Nanoseconds);
    }
}
