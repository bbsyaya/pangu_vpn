package org.turing.vpn.response;

public class VpnTaskStatistics {
	//private static final Logger logger = Logger.getLogger(VpnTaskStatistics.class);
	public int getTaskIncrementTotalCount() {
		return taskIncrementTotalCount;
	}
	public void setTaskIncrementTotalCount(int taskIncrementTotalCount) {
		this.taskIncrementTotalCount = taskIncrementTotalCount;
	}
	public int getTaskStockTotalCount() {
		return taskStockTotalCount;
	}
	public void setTaskStockTotalCount(int taskStockTotalCount) {
		this.taskStockTotalCount = taskStockTotalCount;
	}
	public int getTaskAllocIncrementCount() {
		return taskAllocIncrementCount;
	}
	public void setTaskAllocIncrementCount(int taskAllocIncrementCount) {
		this.taskAllocIncrementCount = taskAllocIncrementCount;
	}
	public int getTaskAllocStockCount() {
		return taskAllocStockCount;
	}
	public void setTaskAllocStockCount(int taskAllocStockCount) {
		this.taskAllocStockCount = taskAllocStockCount;
	}
	public int getTaskReportFinishedCount() {
		return taskReportFinishedCount;
	}
	public void setTaskReportFinishedCount(int taskReportFinishedCount) {
		this.taskReportFinishedCount = taskReportFinishedCount;
	}
	public int getTaskReportNotFinishedCount() {
		return taskReportNotFinishedCount;
	}
	public void setTaskReportNotFinishedCount(int taskReportNotFinishedCount) {
		this.taskReportNotFinishedCount = taskReportNotFinishedCount;
	}
	//-------------小任务池------------------------------
	private int taskIncrementTotalCount = 0; // 需完成增量任务数
	private int taskStockTotalCount= 0; // 需完成的存量任务数
	private int taskAllocIncrementCount= 0; // 已分配增量的数量
	private int taskAllocStockCount= 0; // 已分配存量的数量
	private int taskReportFinishedCount= 0; // 上报已正常完成的数量
	private int taskReportNotFinishedCount= 0; // 上报非正常完成的数量
	//--------------------------------------------------------
	public boolean isCanAllocIncrement(){
		if(taskIncrementTotalCount > taskAllocIncrementCount)
			return true;
		
		return false;
	}
	
	public boolean isCanAllocStock(){
		if(taskStockTotalCount > taskAllocStockCount)
			return true;
		
		return false;
	}
	
	public boolean isTaskFinished(){
		if(taskIncrementTotalCount + taskStockTotalCount == taskReportFinishedCount + taskReportNotFinishedCount)
			return true;
		
		return false;
	}
	public void print(){
		StringBuffer buf = new StringBuffer();
		buf.append("\n taskIncrementTotalCount:" + taskIncrementTotalCount);
		buf.append("\n taskStockTotalCount:" + taskStockTotalCount);
		buf.append("\n taskAllocIncrementCount:" + taskAllocIncrementCount);
		buf.append("\n taskAllocStockCount:" + taskAllocStockCount);
		buf.append("\n taskReportFinishedCount:" + taskReportFinishedCount);
		buf.append("\n taskReportNotFinishedCount:" + taskReportNotFinishedCount);
		//logger.info(buf.toString());
	}
}
