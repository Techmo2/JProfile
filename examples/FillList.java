import net.devmentality.jprofile.JProfile;

import java.util.ArrayList;
import java.util.Random;

/**
 * In this example, we profile a function that fills a list with random numbers.
 */
public class FillList {
    private static Random r = new Random(System.currentTimeMillis());

    private static void filllist(ArrayList<Integer> list, int size){
        list.clear();
        for(int i = 0; i < size; i++){
            list.add(r.nextInt());
        }
    }

    public static void main(String[] args){
        int min_size = 1;
        int max_size = 1048576;

        // Create a profile with a maximum of 1000 data points (we don't use them all)
        JProfile<Integer> factorialProfile = new JProfile<>("Fill List Function", 1000);

        ArrayList<Integer> list = new ArrayList<Integer>();

        for(int size = min_size; size <= max_size; size *= 2){

            // Create a new data point and start recording the execution time with 'size' as the x value of the data point.
            factorialProfile.startSample(size);

            // Run the factorial function. We do it 100 times to increase accuracy.
            for(int i = 0; i < 100; i++){
                filllist(list, size);
            }

            // Stop recording and save the execution time in the data point. Tell the profile we ran filllist 100 times.
            // It will use this information to calculate the actual execution time per single function call.
            factorialProfile.stopSample(size, 100);
        }

        // Export the data to a .csv file with microseconds for the time scale
        factorialProfile.export(JProfile.Microseconds);
    }
}
