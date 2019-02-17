/**
 * 
 */
package com.batch.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author hjadhav1
 *
 */
public class BatchDaoImpl implements BatchDao {

	public JdbcTemplate jdbcTemplate;

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public int UpdateTable() {
		return jdbcTemplate.update("UPDATE Alerts SET ALERTAGE=ALERTAGE+1");
	}

}
