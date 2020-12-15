package simulator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public final class StatisticsSaver {

    String path;

    public StatisticsSaver(){

        File statisticsFolder = new File("statistics");
        if(!statisticsFolder.exists()){
           if(!statisticsFolder.mkdir()){
               throw new RuntimeException("can't create statistics directory");
           }
        }

        path = "statistics/" + this.toString();

        File thisStatisticsFolder = new File(path);
        if(!thisStatisticsFolder.mkdir()){
            throw new RuntimeException("can't create "+ path +" directory");
        }



    }


    public void saveStatistics(StatisticsPack statisticsPack,String fileName){

        try(Writer writer = new FileWriter(path +"/" + fileName)){
            writer.append(statisticsPack.toString());
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

}
