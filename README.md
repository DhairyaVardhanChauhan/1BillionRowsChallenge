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

## Performance Benchmark

### System Specifications

| Component | Specification |
|-----------|--------------|
| **CPU** | Load Avg: 3.61, 3.77, 4.31 |
| **CPU Usage** | 17.49% user, 16.78% sys, 65.72% idle |
| **Physical Memory** | 7.6GB (7467M used: 1321M wired, 3771M compressor, 166M unused) |
| **Virtual Memory** | 210TB vsize, 5.7GB framework vsize |
| **Processes** | 399 total (4 running, 395 sleeping, 3478 threads) |
| **Disk I/O** | Read: 1.2TB, Write: 988GB |
| **Network I/O** | In: 7.7GB (6.8M packets), Out: 1.6GB (2.2M packets) |

### Benchmark Results

| Approach | Time (seconds) | Notes |
|----------|----------------|-------|
| **Brute Force** | 366s (~6 minutes) | Baseline implementation |

> 💡 **Challenge Goal**: Optimize the solution to process 1 billion rows significantly faster than the brute force approach!

## Why This Challenge?

This challenge demonstrates:
- File I/O optimization techniques
- Memory management strategies
- Parallel processing capabilities
- JVM performance tuning
- Creative problem-solving in Java

Happy coding! 🚀
