package org.bizzdeskgroup.Dtos.Query;

public class AgentCashList {
    private double Amount;
    private int Count;
    private String Agent;
    private String Mda;
//    private String Project;

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getAgent() {
        return Agent;
    }

    public void setAgent(String agent) {
        Agent = agent;
    }

    public String getMda() {
        return Mda;
    }

    public void setMda(String mda) {
        Mda = mda;
    }

//    public String getProject() {
//        return Project;
//    }
//
//    public void setProject(String project) {
//        Project = project;
//    }
}
