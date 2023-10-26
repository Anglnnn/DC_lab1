package PartB;

public class CustomSemaphore {
    private volatile int semaphore; // 1 is free

    public CustomSemaphore() {
        this.semaphore = 1; // free by default
    }

    public void lock() {
        semaphore = 0;
    }

    public void free() {
        semaphore = 1;
    }

    public boolean isLocked() {
        return semaphore == 0;
    }

    public boolean isFree() {
        return semaphore == 1;
    }
}
