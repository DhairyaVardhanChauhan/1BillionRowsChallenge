# One Billion Row Challenge

## Performance Analysis & Optimization Guide

## By version 4 we are down from 6mins in version-1 to just 26s in version-4 :)

### The CPU Underutilization Problem 🔍

**CPU Usage: Only 64.3%**

<img width="1026" height="625" alt="CPU Monitor showing 64.3% utilization" src="https://github.com/user-attachments/assets/8b94a294-518e-41a5-b2c7-e9881f4af3e1" />

The profiler reveals:
- **CPU usage: 64.3%** - significant headroom remaining
- **GC activity: 0.0%** - garbage collection isn't the bottleneck
- Only **~60% average CPU utilization** during processing

---

### Thread Timeline Analysis

<img width="1042" height="680" alt="Thread timeline visualization" src="https://github.com/user-attachments/assets/49c00d84-3a0a-439a-8ff0-7398f25f1977" />

Key observations:
- **8 worker threads** (Thread-0 through Thread-7) running for the full duration
- All threads show **100% running time** individually
- However, system-wide CPU usage is only ~64%
- Threads are spending time waiting on I/O operations

---

### Thread Profiling Details

<img width="1059" height="298" alt="Thread profiling details" src="https://github.com/user-attachments/assets/4655d80c-e69d-48ef-b199-bd671fbfaad7" />

**Critical Finding:**
- All 8 threads are utilizing 100% of the CPU individually, which means no thread is sitting idle.

---

## 🔑 Key Optimizations (In Brief)

### 🚩 You are I/O bound, not CPU bound.

Even though 8 threads are "running", they're waiting on disk / memory access.


### 1) Split File into Independent Chunks

Divide file into equal byte ranges per thread:

```java
long chunkSize = fileSize / numThreads;
```

Each thread processes its own mapped region.

**Benefits:**
- ✔ No shared file pointer
- ✔ No locking
- ✔ Better parallelism

---

### 2) Align to Newline Boundaries

After computing chunk start:

```java
while (buffer.get() != '\n') {}
```

**Benefits:**
- ✔ Prevents broken lines
- ✔ Avoids duplicate/partial records

---

### 3) Parse Bytes Directly (No Strings)

**Avoid:**
- `split()`
- `substring()`
- `new String()`

**Instead:** Parse directly from `byte[]`.

```java
static double fastParseDouble(byte[] str, int start, int end) {
    // Direct byte parsing without String creation
    // ... implementation
}
```

**Benefits:**
- ✔ No allocations
- ✔ No GC pressure
- ✔ Much faster

---

### 5️⃣ Use Thread-Local Aggregation

Each thread keeps its own stats map, then merge at the end.

```java
// Each thread maintains independent state
Map<String, Stats> result = new HashMap<>(16_384);

// Merge only at the very end
finalMap.merge(entry.getKey(), entry.getValue(), (a, b) -> {
    a.min = Math.min(a.min, b.min);
    a.max = Math.max(a.max, b.max);
    a.sum += b.sum;
    a.count += b.count;
    return a;
});
```

**Benefits:**
- ✔ Avoids contention
- ✔ Maximizes CPU efficiency
- ✔ Lock-free processing

---

## Summary

The profiling data shows that while all threads are actively running (100% running time), the overall CPU utilization is only 64.3% due to **I/O bottlenecks**. The threads are blocked waiting for disk reads rather than being CPU-limited.

By implementing memory-mapped I/O and the other optimizations listed above, we can reduce I/O wait times and push CPU utilization closer to 90-100%, significantly improving overall performance.

---

**Next Steps:** Implement these optimizations to eliminate the I/O bottleneck and achieve near-linear scaling with the number of CPU cores.
