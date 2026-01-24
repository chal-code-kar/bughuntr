package com.tcs.utx.digiframe.util;

import java.io.Serializable;
import java.util.Comparator;
import com.tcs.utx.digiframe.dto.UtxMenuItemServiceVo;

public class SSAComparator implements Comparator<UtxMenuItemServiceVo>, Serializable {
	private static final long serialVersionUID = 1;

	@Override
	public int compare(UtxMenuItemServiceVo menu1, UtxMenuItemServiceVo menu2) {
		double sequenceOrder1 = menu1.getOrder_no();
		double sequenceOrder2 = menu2.getOrder_no();

		if (sequenceOrder1 == sequenceOrder2) {
			return 0;
		} else if (sequenceOrder1 > sequenceOrder2) {
			return 1;
		} else {
			return -1;
		}
	}
}
