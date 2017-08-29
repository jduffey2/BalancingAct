package com.example.jduff.balancingact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Balancing Act
 * Description: The Main class to create and play a puzzle
 * Handles generating itself, based on input parameters and will accept a solution and check
 * if it is a valid solution, should only be 1 solution but will accept any valid solution
 * Author: jduff
 * Date: 3/19/2017
 *

 */

class BalancingAct implements Serializable{
    private Difficulty difficulty;
    private int target;
    private int groupCount;
    private int elementCount;
    private boolean negs_allowed;
    private int reduction;
    private ArrayList<Integer> numbers;
    private ArrayList<ArrayList<Integer>> groupList;


    BalancingAct( Difficulty diff) {
        difficulty = diff;
    }
    public BalancingAct() {difficulty = Difficulty.BEGINNER; }

    void createPuzzle() {
        groupList = new ArrayList<>();
        generateParams();
        generatePuzzle();
        if(negs_allowed) {
            target -= reduction;
        }

    }

    private void generatePuzzle() {
        ArrayList<ArrayList<int[]>> powersetList = new ArrayList<>();
        while (groupList.size() < groupCount) {
            ArrayList<Integer> group1 = generateGroup();

            if(checkGroup(powersetList, group1)) {
                powersetList.add(generatePowersetSum(group1));
                groupList.add(group1);
            }
        }

        numbers = new ArrayList<>();
        for(ArrayList<Integer> group : groupList ) {
            for(int element : group) {
                numbers.add(element);
            }
        }
        Collections.sort(numbers);
    }

    private void generateParams() {
        int min, max;
        boolean negs;
        int[] groupsP, elementsP;

        switch (difficulty) {
            case BEGINNER:
                min = 15;
                max = 100;
                groupsP = new int[]{2};
                elementsP = new int[]{3};
                negs = false;
                break;
            case EASY:
                min = 20;
                max = 300;
                groupsP = new int[]{2,3};
                elementsP = new int[]{3,4};
                negs = false;
                break;
            case MEDIUM:
                min = 200;
                max = 1000;
                groupsP = new int[]{2,3,4};
                elementsP = new int[]{3,4,5,6};
                negs = (Math.random() < 0.3);
                break;
            case HARD:
                min = 900;
                max = 1800;
                groupsP = new int[]{3,4};
                elementsP = new int[]{5,6,7};
                negs = (Math.random() < 0.5);
                break;
            case EXPERT:
                min = 1500;
                max = 5000;
                groupsP = new int[]{3,4,5};
                elementsP = new int[]{5,6,7,8};
                negs = (Math.random() < 0.6);
                break;
            default:
                min = 15;
                max = 100;
                groupsP = new int[]{2};
                elementsP = new int[]{3};
                negs = false;
        }

        int range = max - min;
        target = Math.round((int)(Math.random() * range)) + min;
        groupCount = groupsP[(int)Math.floor(Math.random() * groupsP.length)];
        elementCount = elementsP[(int)Math.floor(Math.random() * elementsP.length)];
        negs_allowed = negs;
        if(negs) {
            reduction = (int)Math.round(target * ((Math.random() * 0.6) + 0.1));
        }
    }

    private ArrayList<Integer> generateGroup() {
        while(true) {
            ArrayList<Double> group = new ArrayList<>(), reduct = new ArrayList<>();
            ArrayList<Integer> reductFinal = new ArrayList<>();
            int reductTotal, total;
            group.add(0.0);

            if(negs_allowed) {
                reduct.add(0.0);
                reductTotal = 0;
            }

            for(int i = 0; i < elementCount - 1; i++) {
                group.add(Math.random());
                if(negs_allowed) {
                    reduct.add(Math.random());
                }
            }

            group.add(1.0);
            Collections.sort(group);

            if(negs_allowed) {
                reduct.add(1.0);
                Collections.sort(reduct);
            }

            ArrayList<Integer> fin = new ArrayList<>();
            total = 0;
            reductTotal = 0;

            for(int j = 1; j < group.size(); j++) {
                int value = (int)Math.round((group.get(j) - group.get(j - 1)) * target);
                fin.add(value);
                total += value;

                if(negs_allowed) {
                    int redValue = (int)Math.round(reduct.get(j) - reduct.get(j - 1) * reduction);
                    reductFinal.add(redValue);
                    reductTotal += redValue;
                }
            }

            fin.set(elementCount - 1, (fin.get(elementCount - 1) + (target - total)));

            if(negs_allowed) {
                reductFinal.set(elementCount - 1, (reductFinal.get(elementCount - 1) + (reduction - reductTotal)));
                for(int k = 0; k < fin.size(); k++) {
                    fin.set(k, (fin.get(k) - reductFinal.get(k)));
                }
            }

            if( fin.contains(0)) {
                continue;
            }

            Set<Integer> set = new HashSet<>(fin);
            if(set.size() == fin.size()) {
                return fin;
            }
        }
    }

    private ArrayList<int[]> generatePowersetSum(ArrayList<Integer> group) {
        int i, mask, total;
        ArrayList<int[]> powerset = new ArrayList<>();
        ArrayList<Integer> result;


        total = (int)Math.pow(2, group.size());
        for(mask = 1; mask < total - 1; mask++) {
            result = new ArrayList<>();
            i = group.size() - 1;
            do {
                if( (mask & (1 << i)) != 0) {
                    result.add(group.get(i));
                }
            } while(i-- > 0);

            int sum = 0;
            for(int j = 0; j < result.size(); j++) {
                sum += result.get(j);
            }

            powerset.add(new int[]{result.size(), sum});
        }

        return powerset;
    }

    private boolean checkGroup(ArrayList<ArrayList<int[]>> powerSetList, ArrayList<Integer> potentialGroup) {
        ArrayList<int[]> powerset = generatePowersetSum(potentialGroup);

        for (int x = 0; x <powerset.size(); x++) {
            int[] subset = powerset.get(x);
            for(int y = 0; y < powerSetList.size(); y++) {
                ArrayList<int[]> group = powerSetList.get(y);
                for(int z = 0; z < group.size(); z++) {
                    int[] sset = group.get(z);

                    if(sset[0] == subset[0] && sset[1] == subset[1]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    boolean checkAnswer(ArrayList<ArrayList<Integer>> solution) {
        for(ArrayList<Integer> group: solution) {
            if(group.size() != elementCount) {
                return false;
            }
            int sum = 0;
            for(int element : group) {
                sum += element;
            }
            if(sum != target) {
                return false;
            }
        }
        return true;
    }

    int getTarget() {
        return target;
    }
    int getGroupCount() {
        return groupCount;
    }
    int getElementCount() {
        return elementCount;
    }
    int getReduction() {
        return reduction;
    }
    boolean isNegs_allowed() {
        return negs_allowed;
    }
   ArrayList<Integer> getNumbers() {
        return numbers;
    }
    ArrayList<ArrayList<Integer>> getGroupList() {
        return groupList;
    }
    Difficulty getDifficulty() { return difficulty; }

    void setPuzzle(Difficulty diff, int targ, int groups, int elements, boolean negs, int reduct, ArrayList<Integer> nums, ArrayList<ArrayList<Integer>> groupL) {
        difficulty = diff;
        target = targ;
        groupCount = groups;
        elementCount = elements;
        negs_allowed = negs;
        reduction = reduct;
        numbers = nums;
        groupList = groupL;
    }
}
