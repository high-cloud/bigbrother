package ustc.yyd.bigbrother.webserver.data;

import ustc.yyd.bigbrother.data.Machine;

import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    public static ConcurrentHashMap<String, Machine> machineMap = new ConcurrentHashMap<>();
}
