public class PetName {
    private final int maxCount = 10;
    private String[] name;

    public PetName() {
        this.name = new String[maxCount];
        name[0] = "bird";
        name[1] = "bear";
        name[2] = "cat";
        name[3] = "chicken";
        name[4] = "crab";
        name[5] = "croc";
        name[6] = "shark";
        name[7] = "shrimp";
        name[8] = "sloth";
        name[9] = "squid";
    }

    public String getRandomName() {
        int i = (int)(Math.random()*(9));
        return name[i];
    }
}
