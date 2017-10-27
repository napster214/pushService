/**
 * <p>Title: ErrorHandler</p>
 * <p>Description: 用于处理Struts中抛出的应用错误</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 
 * @version 1.0
 * 2006-9-18
 */

package com.jadlsoft.struts.exceptions;

import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jadlsoft.exceptions.BaseAppException;
import com.jadlsoft.utils.IConstants;

/**
 * @author libanggui
 * 
 */
public class ErrorHandler {

	public static ActionMessages ProcessAppError(BaseAppException be) {
		ActionMessages errors = new ActionMessages();

		List exceptions = be.getExceptions();
		if (exceptions != null && !exceptions.isEmpty()) {
			Iterator item = exceptions.iterator();

			while (item.hasNext()) {
				BaseAppException subException = (BaseAppException) item.next();

				proceBaseException(errors, subException);
			}
		} else if (be.getMessageKey() != null && !"".equals(be.getMessageKey())) {
			proceBaseException(errors, be);
		}

		return errors;
	}

	private static void proceBaseException(ActionMessages errors,
			BaseAppException ex) {

		ActionMessage message = null;

		String errorCode = ex.getMessageKey();

		Object[] args = ex.getMessageArgs();

		if (args != null && args.length > 0) {
			message = new ActionMessage(errorCode, args);
		} else {
			message = new ActionMessage(errorCode);
		}
		errors.add(IConstants.MESSAGE_KEY, message);
	}

	/**
	 * getSuccessMessage() 功能：根据mess决定提示信息
	 * 
	 * @param mess
	 * @return ActionMessages
	 */
	public static ActionMessages getMessage(String mess) {
		ActionMessages msgs = new ActionMessages();
		msgs.add(IConstants.MESSAGE_KEY, new ActionMessage(mess));
		return msgs;
	}
	
	public static ActionMessages getMessage(String mess, Object[] params) {
		ActionMessages msgs = new ActionMessages();
		msgs.add(IConstants.MESSAGE_KEY, new ActionMessage(mess, params));
		return msgs;
	}

	/**
	 * getSuccessMessage() 功能：获取操作成功的提示信息
	 * 
	 * @return ActionMessages
	 */
	public static ActionMessages getSuccessMessage() {
		ActionMessages msgs = new ActionMessages();
		msgs.add(IConstants.MESSAGE_KEY, new ActionMessage("global.success"));
		return msgs;
	}
}
