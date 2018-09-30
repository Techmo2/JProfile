# JProfile
JProfile is a simple tool that allows for the easy analysis of code. It automatically keeps track of function performance and exports the results in csv format for use in excel and other spreadsheet software.

## Example
```Java
public class RandomSort {
    private static Random r = new Random(System.currentTimeMillis());

    // Generates a list of random numbers
    public static ArrayList<Integer> generateList(int size){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < size; i++){
            list.add(r.nextInt());
        }
        return list;
    }

    public static void main(String[] args){
        int min_length = 0;
        int max_length = 100000;

        int iterations_per_sample = 10;

        ArrayList<Integer> list;

        // Create a new profiler. We specify that the data type for it's x axis is 'Integer'.
        // We also specify that it can hold a maximum of 1000 data points, but we won't use all of them.
        JProfile<Integer> profile = new JProfile<>("Random List Function", 1000);

        // Collect different data points by using our function with different inputs
        for(int length = min_length; length <= max_length; length += 1000){

            // Start a new data point
            profile.startSample(length);

            // Do multiple iterations per sample to get a more accurate result
            for(int j = 0; j < iterations_per_sample; j++){

                // We can pause the profiler to prevent certain sections of code from affecting our results
                profile.pauseSample(length);
                list = generateList(length);
                profile.resumeSample(length);

                // Profile Java's sort method
                Collections.sort(list);
            }
            // Stop sampling and save the data point
            profile.stopSample(length, iterations_per_sample);
        }

        // Export all recorded data points to a .csv file
        // This can be opened in excel or other spreadsheet applications
        profile.export(JProfile.Milliseconds);
    }
}
```

## Downloads
If you just want a convenient jar to include in your code, you can download the jar release [here](https://drive.google.com/file/d/1FJWlKSx6_34mIH4wT1y4jeg1_ceDkRnf/view?usp=sharing).
