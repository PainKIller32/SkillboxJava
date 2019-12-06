public class Changes {
    private final long start;
    private final long end;
    private final int increment;
    private final String text;

    public Changes(long start, long end, int increment, String text) {
        this.start = start;
        this.end = end;
        this.increment = increment;
        this.text = text;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public int getIncrement() {
        return increment;
    }

    public String getText() {
        return text;
    }
}

