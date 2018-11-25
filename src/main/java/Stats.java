public class Stats {

    private int nbOfPerson;
    private int nbOfJobless;
    private int nbOfMarried;
    private int nbOfSingle;
    private int averageAge;
    private int nbOfPersonAboveAverageAge;
    private int averageSalary;
    private int averageSalaryAdjusted;
    private int totalNbOfChildren;


    public Stats(int nbOfPerson, int nbOfJobless, int nbOfMarried, int nbOfSingle, int averageAge, int nbOfPersonAboveAverageAge,
                    int averageSalary, int averageSalaryAdjusted, int totalNbOfChildren) {
        this.nbOfPerson = nbOfPerson;
        this.nbOfJobless = nbOfJobless;
        this.nbOfMarried = nbOfMarried;
        this.nbOfSingle = nbOfSingle;
        this.averageAge = averageAge;
        this.nbOfPersonAboveAverageAge = nbOfPersonAboveAverageAge;
        this.averageSalary = averageSalary;
        this.averageSalaryAdjusted = averageSalaryAdjusted;
        this.totalNbOfChildren = totalNbOfChildren;
    }

    // Getters
    public int getNbOfPerson() {
        return nbOfPerson;
    }

    public int getNbOfJobless() {
        return nbOfJobless;
    }

    public int getNbOfMarried() {
        return nbOfMarried;
    }

    public int getNbOfSingle() {
        return nbOfSingle;
    }

    public int getAverageAge() {
        return averageAge;
    }

    public int getNbOfPersonAboveAverageAge() {
        return nbOfPersonAboveAverageAge;
    }

    public int getAverageSalary() {
        return averageSalary;
    }

    public int getAverageSalaryAdjusted() {
        return averageSalaryAdjusted;
    }

    public int getTotalNbOfChildren() {
        return totalNbOfChildren;
    }
}
