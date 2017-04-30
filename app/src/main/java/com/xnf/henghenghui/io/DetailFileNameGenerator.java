package com.xnf.henghenghui.io;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;

public class DetailFileNameGenerator implements FileNameGenerator {

	@Override
	public String generate(String imageUri) {
		String oldUri=imageUri;
		oldUri= oldUri.replace(":", "");
		oldUri= oldUri.replace("//", "/");
		return oldUri;
	}

}
