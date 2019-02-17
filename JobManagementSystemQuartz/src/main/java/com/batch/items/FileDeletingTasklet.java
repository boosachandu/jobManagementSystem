/**
 * 
 */
package com.batch.items;

import java.io.File;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author hjadhav1
 *
 */
public class FileDeletingTasklet implements Tasklet, InitializingBean{
	
	private Resource resource;

	/**
	 * @param resource the resource to set
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(resource, "Directory must be set");
		
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		File file = resource.getFile();
		if(file.exists())
		{
			boolean deleted = file.delete();
			if(!deleted)
			{
				throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
			}
		}
		return RepeatStatus.FINISHED;
	}
	

}
