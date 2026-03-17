package org.tuc;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.tuc.avl.AVLTree;
import org.tuc.bst.BSTree;
import org.tuc.btree.BTree;
import org.tuc.counter.MultiCounter;
import org.tuc.interfaces.SearchInsert;
import org.tuc.linearhashing.LinearHashing;

public class Measurements {

    private static int[] readKeysFromFile(String fileName, int N) throws IOException {
        int[] keys = new int[N];
        
        try (FileInputStream fis = new FileInputStream(fileName)) {
            byte[] buffer = new byte[N * 4];
            fis.read(buffer);
            
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            
            for (int i = 0; i < N; i++) {
                keys[i] = byteBuffer.getInt();
            }
        }
        
        return keys;
    }
    protected static int[] getDistinctRandomInts(int N, int K) {
        Random randomGenerator = new Random();
        int[] randomInts = randomGenerator.ints(0, 3 * N + 1).distinct().limit(K).toArray();

        return randomInts;
    }

    public static void main(String[] args) {
        
        int[] NValues = {
            20, 50, 100, 200, 1000, 2500, 5000, 10000, 20000, 40000, 
            60000, 80000, 100000, 200000, 1000000, 1500000, 2000000, 2500000, 3000000
        }; 

        List<int[]> keysList = new ArrayList<>();
    
        for(int N : NValues){
            String fileName = "numbers-" + N + ".bin";
            try {
                int[] keys = readKeysFromFile(fileName, N);
                keysList.add(keys);
                //System.out.println("Read " + N + " keys from " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int K;

        ArrayList resultHeadingsTimeInsert = new ArrayList<>();
        ArrayList resultHeadingsTimeSearch = new ArrayList<>();
        ArrayList resultHeadingsLevelsSearch = new ArrayList<>();


        resultHeadingsTimeInsert.add("MEAN TIME TO INSERT");
        resultHeadingsTimeSearch.add("MEAN TIME TO SEARCH");
        resultHeadingsLevelsSearch.add("MEAN LEVELS FOR SEARCH");


        TestDataCollector collectorTimeInsert = new TestDataCollector(resultHeadingsTimeInsert);
        TestDataCollector collectorTimeSearch = new TestDataCollector(resultHeadingsTimeSearch);
        TestDataCollector collectorLevelsSearch = new TestDataCollector(resultHeadingsLevelsSearch);

        // Insert keys into each data structure
        for (int[] keys : keysList) {
            ArrayList<Object> measurementsTimeInsert = new ArrayList<>();
            ArrayList<Object> measurementsTimeSearch = new ArrayList<>();
            ArrayList<Object> measurementsLevelsSearch = new ArrayList<>();


            measurementsTimeInsert.add("  "+keys.length + "  ");
            measurementsTimeSearch.add("  "+keys.length + "  ");
            measurementsLevelsSearch.add("  "+keys.length + "  ");


            AVLTree avlTree = new AVLTree();
            BSTree bsTree = new BSTree();
            BTree bTree_ord100 = new BTree(50);         //here order n is defined as up to 2*n keys
            BTree bTree_ord600 = new BTree(300);
            LinearHashing linearHashing_size40 = new LinearHashing(40, 500);
            LinearHashing linearHashing_size1000 = new LinearHashing(1000, 500);

            SearchInsert[] treesAndHashings = {
                avlTree,
                bsTree,
                bTree_ord100,
                bTree_ord600,  
                linearHashing_size40,
                linearHashing_size1000
            };

            for (SearchInsert struct : treesAndHashings) {
                if(keys.length == 20){
                    if(struct.getClass() != BTree.class && struct.getClass() != LinearHashing.class){
                        resultHeadingsTimeInsert.add(struct.getClass().getSimpleName());
                        resultHeadingsTimeSearch.add(struct.getClass().getSimpleName());
                        resultHeadingsLevelsSearch.add(struct.getClass().getSimpleName());

                    }
                    else if(struct.getClass() == BTree.class){
                        resultHeadingsTimeInsert.add(struct.getClass().getSimpleName() + (((BTree) struct).getT() == 50 ? "100" : "600"));
                        resultHeadingsTimeSearch.add(struct.getClass().getSimpleName() + (((BTree) struct).getT() == 50 ? "100" : "600"));
                        resultHeadingsLevelsSearch.add(struct.getClass().getSimpleName() + (((BTree) struct).getT() == 50 ? "100" : "600"));

                    }
                    else{
                        resultHeadingsTimeInsert.add(struct.getClass().getSimpleName() + (((LinearHashing) struct).getBucketSize() == 40 ? "40" : "1000"));
                        resultHeadingsTimeSearch.add(struct.getClass().getSimpleName() + (((LinearHashing) struct).getBucketSize() == 40 ? "40" : "1000"));
                        resultHeadingsLevelsSearch.add(struct.getClass().getSimpleName() + (((LinearHashing) struct).getBucketSize() == 40 ? "40" : "1000"));
                    }        
                }

                for (int key : keys) {
                   struct.insert(key);
                }
            
                K = (keys.length < 201) ? 10 : (keys.length < 1001) ? 50 : 100;

                int randomIntsToInsert[] = getDistinctRandomInts(keys.length, K);
                int randomIntsToSearch[] = getDistinctRandomInts(keys.length, K);


                
                long totalTimeInsert = 0;
                for (int key : randomIntsToInsert) {
                    long startTime = 0;

                    startTime = System.nanoTime(); // we want to measure only the insert part
                    struct.insert(key);
                    totalTimeInsert = totalTimeInsert + (System.nanoTime() - startTime);
                }

                double meanTotalInsertTime = (double) totalTimeInsert / K;
                String formattedMeanTotalInsertTime = String.format("%.2f", meanTotalInsertTime);


                measurementsTimeInsert.add(formattedMeanTotalInsertTime);

                long totalTimeSearch = 0, totalLevelsSearch = 0;
                for (int key : randomIntsToSearch) {
                    long startTime = 0;

                    MultiCounter.resetCounter(1);
                    startTime = System.nanoTime(); // we want to measure only the insert part
                    struct.searchKey(key);
                    totalTimeSearch = totalTimeSearch + (System.nanoTime() - startTime);
                    totalLevelsSearch = totalLevelsSearch + MultiCounter.getCounter(1);
                }

                double meanTotalSearchTime = (double) totalTimeSearch / K;
                String formattedMeanTotalSearchTime = String.format("%.2f", meanTotalSearchTime);

                double meanTotalSearchLevels = (double) totalLevelsSearch /K;
                String formattedMeanTotalSearchLevels = String.format("%.2f", meanTotalSearchLevels);


                measurementsTimeSearch.add(formattedMeanTotalSearchTime);
                measurementsLevelsSearch.add(formattedMeanTotalSearchLevels);
                
            }
            collectorTimeInsert.addRow(measurementsTimeInsert);
            collectorTimeSearch.addRow(measurementsTimeSearch);
            collectorLevelsSearch.addRow(measurementsLevelsSearch);
        }

        collectorTimeInsert.toScreen();
        collectorTimeSearch.toScreen();
        collectorLevelsSearch.toScreen();

        collectorTimeInsert.toFile("TimeOfInsert" + ".csv");
        collectorTimeSearch.toFile("TimeOfSearch" + ".csv");
        collectorLevelsSearch.toFile("LevelsOfSearch" + ".csv");
    }


}
