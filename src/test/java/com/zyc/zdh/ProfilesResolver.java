package com.zyc.zdh;

import java.util.Calendar;
import java.util.Date;

import org.springframework.test.context.ActiveProfilesResolver;

/**
 * ClassName: ProfilesResolver
 * 
 * @author zyc-admin
 * @date 2018年3月6日
 * @Description: TODO
 */
public class ProfilesResolver implements ActiveProfilesResolver {

	@Override
	public String[] resolve(Class<?> testClass) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;// 从星期日开始
		if (week < 5 && week > 0) {
			return new String[] { "pro" };
		} else {
			return new String[] { "dev" };
		}
	}

}
