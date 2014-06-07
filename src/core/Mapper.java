package core;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Mapper implements DataMapper {

    public static final String DATA_EXT = ".data";
    public static final String CONF_EXT = ".conf";
    static String directory;
    static String configDirectory;
    static String counters;

    public Mapper() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("C:\\Users\\Admin\\IdeaProjects\\JavaCodeArt\\core.DataMapper\\src\\config.txt"));
        this.directory = props.getProperty("directory");
        this.configDirectory = props.getProperty("configDirectory");
        this.counters = props.getProperty("counters");
    }


    private File getConfFile(Class clazz, boolean create) throws DataMapperException {
        File configFolder = new File(configDirectory);
        File[] configFiles = configFolder.listFiles();


        if (configFiles == null && create == true) {
            configFolder.mkdir();

        }

        if (configFiles != null) {
            for (File file : configFiles) {
                if (file.getName().equals(clazz.getSimpleName() + CONF_EXT)) {
                    return file;
                }
            }
        }
        if (create) {
            File confFile = new File(configDirectory + clazz.getSimpleName() + CONF_EXT);
            try {
                confFile.createNewFile();
                FileWriter fw = new FileWriter(confFile, false);
                for (Field field : clazz.getDeclaredFields()) {
                    fw.write(field.getName() + "\n");
                }
                fw.close();
            } catch (IOException e) {
                throw new DataMapperException();
            }
            return confFile;
        }
        return null;
    }

    private File getDataFile(Class clazz, boolean create) throws DataMapperException {
        File dataFolder = new File(directory);
        File[] dataFiles = dataFolder.listFiles();
        if (dataFiles == null && create) {
            File dataFile = new File(directory + clazz.getSimpleName() + DATA_EXT);
            try {
                dataFolder.mkdir();
                dataFile.createNewFile();
                return dataFile;
            } catch (IOException e) {
                throw new DataMapperException();
            }
        } else {
            for (File file : dataFiles) {
                if (file.getName().equals(clazz.getSimpleName() + DATA_EXT)) {
                    return file;
                }
            }
            if (create) {
                File dataFile = new File(directory + clazz.getSimpleName() + DATA_EXT);
                try {
                    dataFile.createNewFile();
                } catch (IOException e) {
                    throw new DataMapperException();
                }
                return dataFile;
            }
            return null;
        }


    }

    private  int getLastCounter(Class clazz) throws DataMapperException {
        try {
            File countersFile = new File(counters);
            if (countersFile.exists()) {
                Scanner scanCounters = new Scanner(new FileReader(counters));
                if (scanCounters.hasNext()) {
                    while (scanCounters.hasNextLine()) {
                        String line = scanCounters.nextLine();
                        if (line.startsWith(clazz.getSimpleName())) {
                            String[] str = line.split(":");
                            return Integer.parseInt(str[1]);
                        }
                    }

                }


            }

            countersFile.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(countersFile, true));
            bufferedWriter.write(clazz.getSimpleName() + ":" + 0 + "\n");
            bufferedWriter.close();

            return 0;

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataMapperException();
        }

        return 0;
    }

    private  void incrementCounter(Class clazz) throws DataMapperException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(counters));
            String line;
            String input = "";
            while ((line = bufferedReader.readLine()) != null) input += line + '\n';
            int curCounter = getLastCounter(clazz);
            int futureCounter = curCounter + 1;
            input = input.replace(clazz.getSimpleName() + ":" + curCounter, clazz.getSimpleName() + ":" + futureCounter);
            FileOutputStream File = new FileOutputStream(counters);
            File.write(input.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DataMapperException();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public  void save(Object obj) throws DataMapperException {

        File dataFile = getDataFile(obj.getClass(), true);
        File confFile = getConfFile(obj.getClass(), true);
        long counter = getLastCounter(obj.getClass());
        FileWriter fileWriter;
        long currentID = (counter + 1);
        try {
            fileWriter = new FileWriter(dataFile, true);
            fileWriter.write("ID" + currentID);
            Scanner scanner = new Scanner(confFile);
            ArrayList<String> arr = new ArrayList<>();
            while (scanner.hasNextLine()) {
                arr.add(scanner.nextLine());
            }
            for (String str : arr) {
                Field fld = obj.getClass().getDeclaredField(str);
                fld.setAccessible(true);
                fileWriter.write(":" + fld.get(obj));
            }
            fileWriter.write("\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataMapperException();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        incrementCounter(obj.getClass());
        EntityCache.saveToCache(currentID, obj);

    }


    public  Object load(long id, Class clazz) throws DataMapperException {

        File dataFile = getDataFile(clazz, false);
        File confFile = getConfFile(clazz, false);
        if (dataFile == null | confFile == null) {
            System.out.println("No classes were stored");
            return null;
        }


        Scanner scanID = null;
        try {
            scanID = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DataMapperException();
        }

        String[] existingValues = null;
        String idLine;
        while (scanID.hasNextLine()) {
            idLine = scanID.nextLine();
            if (idLine.startsWith("ID" + id)) {
                existingValues = idLine.split(":");
            }
        }

        Scanner scanner = null;
        try {
            scanner = new Scanner(confFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DataMapperException();
        }
        ArrayList<String> arr = new ArrayList<>();
        while (scanner.hasNextLine()) {
            arr.add(scanner.nextLine());
        }

        Object result = null;
        try {
            result = clazz.newInstance();
            for (int i = 0; i < arr.size(); i++) {
                Field f = clazz.getDeclaredField(arr.get(i));
                f.setAccessible(true);

                if (f.getType().equals(String.class)) {
                    f.set(result, existingValues[i + 1]);
                } else if (f.getType().equals(Integer.class)) {
                    f.set(result, Integer.parseInt(existingValues[i + 1]));
                } else if (f.getType().equals(long.class)) {
                    f.set(result, Long.parseLong(existingValues[i + 1]));
                } else if (f.getType().equals(float.class)) {
                    f.set(result, Float.parseFloat(existingValues[i + 1]));
                } else if (f.getType().equals(double.class)) {
                    f.set(result, Double.parseDouble(existingValues[i + 1]));
                } else if (f.getType().equals(short.class)) {
                    f.set(result, Short.parseShort(existingValues[i + 1]));
                } else if (f.getType().equals(int.class)) {
                    f.set(result, Integer.parseInt(existingValues[i + 1]));
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new DataMapperException();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new DataMapperException();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new DataMapperException();
        }


        return result;

    }


    public ArrayList<Object> loadAll(Class clazz) throws DataMapperException {
        File dataFile = getDataFile(clazz, false);
        File confFile = getConfFile(clazz, false);
        if (dataFile == null | confFile == null) {
            System.out.println("No classes were stored");
            return null;
        }


        Scanner scanID = null;
        try {
            scanID = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DataMapperException();
        }

        String[] existingValues = null;
        String idLine;
        ArrayList<Object> result = new ArrayList<>();
        Object resultObject = null;
        while (scanID.hasNextLine()) {
            idLine = scanID.nextLine();

                existingValues = idLine.split(":");

            Scanner scanner = null;
            try {
                scanner = new Scanner(confFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new DataMapperException();
            }
            ArrayList<String> arr = new ArrayList<>();
            while (scanner.hasNextLine()) {
                arr.add(scanner.nextLine());
            }


            try {
                resultObject = clazz.newInstance();
                for (int i = 0; i < arr.size(); i++) {
                    Field f = clazz.getDeclaredField(arr.get(i));
                    f.setAccessible(true);

                    if (f.getType().equals(String.class)) {
                        f.set(resultObject, existingValues[i + 1]);
                    } else if (f.getType().equals(Integer.class)) {
                        f.set(resultObject, Integer.parseInt(existingValues[i + 1]));
                    } else if (f.getType().equals(long.class)) {
                        f.set(resultObject, Long.parseLong(existingValues[i + 1]));
                    } else if (f.getType().equals(float.class)) {
                        f.set(resultObject, Float.parseFloat(existingValues[i + 1]));
                    } else if (f.getType().equals(double.class)) {
                        f.set(resultObject, Double.parseDouble(existingValues[i + 1]));
                    } else if (f.getType().equals(short.class)) {
                        f.set(resultObject, Short.parseShort(existingValues[i + 1]));
                    } else if (f.getType().equals(int.class)) {
                        f.set(resultObject, Integer.parseInt(existingValues[i + 1]));
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new DataMapperException();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new DataMapperException();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new DataMapperException();
            }


            result.add(resultObject);
        }




        return result;
    }


    public  void update(long id, Object obj) throws DataMapperException {
        File dataFile = getDataFile(obj.getClass(), true);
        File confFile = getConfFile(obj.getClass(), true);
        String futureObject = "";
        try {
            Scanner scanner = new Scanner(confFile);
            ArrayList<String> arr = new ArrayList<>();
            while (scanner.hasNextLine()) {
                arr.add(scanner.nextLine());
            }
            for (String str : arr) {
                Field fld = obj.getClass().getDeclaredField(str);
                fld.setAccessible(true);
                futureObject = futureObject + ":" +fld.get(obj);

            }


            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
            String line;
            String input = "";
            while ((line = bufferedReader.readLine()) != null) input += line + '\n';
              if(line.startsWith(String.valueOf(id))){
                  line.replace(id + "\n", id + "" + futureObject + "\n") ;//TODO figure out how to replace the line
              }



            FileOutputStream File = new FileOutputStream(dataFile);
            File.write(input.getBytes());
        } catch (IllegalAccessException | IOException e) {
            e.printStackTrace();


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new DataMapperException();

        }
    }

}
