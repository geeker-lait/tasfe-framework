package com.tasfe.framework.crud.core.sharding;

import com.tasfe.framework.crud.api.ShardingDataSource;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Lait on 2017/8/4.
 */
public class CrudShardingDataSource implements ShardingDataSource{

    public List<DataSource> getWriteDataSources() {
        return null;
    }

    public List<DataSource> getReadDataSources() {
        return null;
    }

    public int getShardingCapacity() {
        return 8000000;
    }

    public int getShardingDatabaseCount() {
        return 4;
    }

    public int getShardingTableCount() {
        return 2;
    }

    public String getShardingDatabaseName() {
        return "test";
    }

    public String getStrcatChar() {
        return "_";
    }
}
