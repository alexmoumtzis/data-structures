package org.tuc.counter;

public class MultiCounter {

	private static long[] counters = new long[10];

	public static void resetCounter(int counterIndex) {
		if (counterIndex - 1 < counters.length)
			counters[counterIndex - 1] = 0;
	}

	public static long getCounter(int counterIndex) {
		if (counterIndex - 1 < counters.length)
			return counters[counterIndex - 1];
		return -1;
	}

	public static boolean increaseCounter(int counterIndex) {
		if (counterIndex - 1 < counters.length)
			// System.out.println("Updating counter " + counterIndex + " with value " +
			// counters[counterIndex - 1]);
			counters[counterIndex - 1]++;
		return true;
	}

	public static boolean increaseCounter(int counterIndex, int step) {
		if (counterIndex - 1 < counters.length)
			counters[counterIndex - 1] = counters[counterIndex - 1] + step;
		return true;
	}

}
