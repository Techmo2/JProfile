# JProfile
JProfile is a simple tool that allows for the easy analysis of code.

## Usage
First, we should include the JProfile class.
```java
import net.devmentality.JProfile;
```
To begin analyzing code, a new profile must first be created. When this is done, we must also specify the name of the profile, and the maximum number of data points it can hold. Any unused capacity will be ignored.

```java
JProfile profile = new JProfile("TimerFunction", 50);
```

After creating the profile, we're ready to start adding data points. In order to get accurate figures, it is highly recommended that you run your algorithm many times per measurement.

```java
// Lets profile our function at 10 different input sizes from 10 to 100
for(int input_size = 10; input_size <= 100; input_size += 10){
    
    // Prepare for the actual profiling.
    // We don't want to do any calculations other than the one we are measuring in the loop
    int[] input = new int[input_size];
    for(int i = 0; i < input_size; i++){
        input[i] = i;
    }
    
    // Now we can begin timing the actual algorithm
    // In the future, JProfile will have tools to make these prepatory steps unnecessary
        
    // Record a new data point and use our input_size as the point on the x axis
    // We can use any data type for our x axis, as long as it is a Comparable type
    profile.startSample(input_size);
    for(int i = 0; i < 100; i++){
        ourCoolAlgorithm(input);
    }
    
    // Stop recording the new data point. Tell JProfile that we ran our function 100 times.
    profile.stopSample(input_size, 100);
}

// Export the data to a csv file
profile.export();
```

## Downloads
If your're not in the compiling mood, you can download the jar release [here](https://drive.google.com/file/d/1FJWlKSx6_34mIH4wT1y4jeg1_ceDkRnf/view?usp=sharing).