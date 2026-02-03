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
| **Model** | MacBook Air (Model Identifier: MacBookAir10,1) |
| **Chip** | Apple M1 |
| **CPU Cores** | 8 cores (4 performance + 4 efficiency) |
| **Memory** | 8 GB |

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
