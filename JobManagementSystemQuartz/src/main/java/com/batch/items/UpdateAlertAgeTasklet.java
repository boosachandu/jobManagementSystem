/**
 * 
 */
package com.batch.items;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.batch.dao.BatchDao;

/**
 * @author hjadhav1
 *
 */
public class UpdateAlertAgeTasklet implements Tasklet, InitializingBean{

	private BatchDao batchDao;
	
	
	public UpdateAlertAgeTasklet(BatchDao batchDaoObj) {
		this.batchDao= batchDaoObj;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		batchDao.UpdateTable();
		return RepeatStatus.FINISHED;
	}

}
