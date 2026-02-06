# One Billion Row Challenge

## What is it?

The **1 Billion Row Challenge** (1BRC) is a fun exploration of how far modern Java can be pushed for aggregating one billion rows from a text file.

The challenge involves processing a text file containing temperature measurements from various weather stations. Each row contains a station name and a temperature measurement. The goal is to calculate the minimum, mean, and maximum temperature for each station as quickly as possible.

### Sample Data Format
```
Hamburg;12.0
Bulawayo;8.9
Palembang;38.8
Hamburg;34.2
```

### The Challenge
- Process **1 billion rows** of temperature data
- Calculate min, mean, and max temperature per station
- Optimize for **speed** and **efficiency**
- File size: ~12GB

## Getting Started

⚠️ **Important**: The data file is approximately **12GB** in size.

Please visit the official repository for detailed instructions on how to generate or download the test data:

**👉 [https://github.com/gunnarmorling/1brc](https://github.com/gunnarmorling/1brc)**

The repository contains:
- Scripts to generate the test data locally
- Baseline implementation
- Submission guidelines
- Performance benchmarks
- Community solutions

## Quick Setup

1. Clone the official repository
2. Follow the instructions in their README to generate the test data
3. Run the baseline implementation or create your own optimized solution

## Why This Challenge?

This challenge demonstrates:
- File I/O optimization techniques
- Memory management strategies
- Parallel processing capabilities
- JVM performance tuning
- Creative problem-solving in Java

---

## Version 3 Insights

### Performance
Completed the task in **85.616s** ⚡

### Key Optimizations 🚀

#### 1. Efficient File Reading
Instead of reading line by line as Strings:
- Read the file in large byte chunks
- Manually detect line breaks (`\n`)
- Parse numbers without creating unnecessary objects
```java
BufferedInputStream bufferedReader = 
    new BufferedInputStream(new FileInputStream(filePath), 1 << 16);
```
- Reads **64 KB** at a time
- Much faster than reading line by line
- Data is stored in a byte array buffer

#### 2. Handling Split Lines
Sometimes a line is cut in half when reading chunks.

**Example:**
```
Lon
don;12.3\n
```

**Solution:**
- Store the partial line in `carry`
- When the next chunk arrives, merge it
- Guarantees every line is parsed correctly
```java
byte[] carry = new byte[256];
int carryLen = 0;
```

Notice the Used Memory has droped a lot from version1

# Version3
<img width="1039" height="443" alt="image" src="https://github.com/user-attachments/assets/0041bf28-446c-48c6-b469-998f8a07c79b" />

# Version1
<img width="833" height="289" alt="image" src="https://github.com/user-attachments/assets/5ad1024f-145f-4ba0-aec0-a32870d99dc2" />


# Insights on both images

Version1
 - Used heap: ~119 MB
 - Heap usage fluctuates high

Needs more memory to do the same work

Version3 

- Used heap: ~35 MB

- Heap usage is much lower

- Does the same work with ~3.5× less memory


---

Happy coding! 🚀
