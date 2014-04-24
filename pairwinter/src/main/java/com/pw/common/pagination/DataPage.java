package com.pw.common.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pairwinter
 * @since 2014-4-15
 * @param <T>
 */
public class DataPage<T> implements Serializable {
    private static final int PAGE_SIZE = 10;
    private int pageSize = PAGE_SIZE;
    private int start;
    private int end;
    private int totalCount;
    private List<T> data;

    public DataPage() {
        this(PAGE_SIZE,0,0,new ArrayList<T>());
    }

    public DataPage(Integer pageSize){
        if(null != pageSize){
            this.pageSize = pageSize;
        }
    }

    public DataPage(Integer pageNo,Integer pageSize){
        if(pageNo != null || pageSize != null){
            this.setStart(pageNo*pageSize);
            this.setEnd(this.getStart()+pageSize);
            this.setPageSize(pageSize);
            this.setData(new ArrayList<T>());
        }
    }

    public DataPage(int pageSize, int start, int totalCount ,List<T> data) {
        this.pageSize = pageSize;
        this.start = start;
        this.totalCount = totalCount;
        this.data = data;
    }

    public int getPageNo(){
        if(totalCount == 0){
            return 1;
        }
        return start/pageSize +1;
    }

    /**
     * get the count of all page.
     * @return
     */
    private int getTotalPage(){
        if(totalCount == 0){
            return 1;
        }
        return (int)Math.ceil(totalCount/pageSize);
    }

    public boolean hasNextPage(){
        return this.getPageNo()<this.getTotalPage();
    }

    public boolean hasPrePage(){
        return this.getPageNo() >1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

