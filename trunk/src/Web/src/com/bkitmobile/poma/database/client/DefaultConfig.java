package com.bkitmobile.poma.database.client;

import java.util.HashMap;

public class DefaultConfig {
	private static final String title = "POMA - Position Manager";
	private static final String lang = "vi";
	private static final String mail = "admins";
	private static final String logo = "/images/poma/logo-header.gif";
	private static final boolean confirm = true;
	private static final String tos = "Nh\u1EEFng qui \u0111\u1ECBnh sau \u0111\u00E2y \u0111\u01B0\u1EE3c \u00E1p d\u1EE5ng \u0111\u1ED1i v\u1EDBi nh\u1EEFng c\u00E1 nh\u00E2n, t\u1ED5 ch\u1EE9c, c\u00F4ng ty, n\u00F3i chung l\u00E0 nh\u1EEFng \u0111\u1ED1i t\u01B0\u1EE3ng s\u1EED d\u1EE5ng th\u00F4ng tin, truy c\u1EADp, khai th\u00E1c th\u00F4ng tin t\u1EEB http://bkitpoma.appspot.com/ \n\n\t1. Qui \u0111\u1ECBnh v\u1EC1 th\u00F4ng tin\nBkitPOMA l\u00E0 \u1EE9ng d\u1EE5ng h\u1ED7 tr\u1EE3 c\u00E1c doanh nghi\u1EC7p, c\u00E1 nh\u00E2n c\u00F3 nhu c\u1EA7u qu\u1EA3n l\u00FD c\u00E1c \u0111\u1ED1i t\u01B0\u1EE3ng. Nh\u1EEFng th\u00F4ng tin tr\u00EAn BkitPOMA n\u00E0y \u0111\u01B0\u1EE3c qui \u0111\u1ECBnh nh\u01B0 sau:\n- Doanh nghi\u1EC7p, c\u00E1 nh\u00E2n khi \u0111\u0103ng k\u00ED s\u1EED d\u1EE5ng d\u1ECBch v\u1EE5 t\u1EA1i BkitPOMA ph\u1EA3i t\u1EF1 ch\u1ECBu tr\u00E1ch nhi\u1EC7m v\u1EC1 quy\u1EC1n c\u00E1 nh\u00E2n c\u1EE7a \u0111\u1ED1i t\u01B0\u1EE3ng b\u1ECB qu\u1EA3n l\u00FD.\n- Ch\u00FAng t\u00F4i ch\u1EC9 \u0111\u1EA3m b\u1EA3o v\u1EDBi nh\u1EEFng \u0111\u01B0\u1EDDng li\u00EAn k\u1EBFt (Link) trong website v\u00E0 kh\u00F4ng \u0111\u1EA3m b\u1EA3o n\u1ED9i dung th\u00F4ng tin \u0111\u1ED1i v\u1EDBi nh\u1EEFng \u0111\u01B0\u1EDDng li\u00EAn k\u1EBFt (Link) n\u1EB1m ngo\u00E0i website.\n- Th\u00F4ng tin t\u1EA1i BkitPOMA KH\u00D4NG c\u00F3 n\u1ED9i dung ch\u00EDnh tr\u1ECB, ph\u1EA3n \u0111\u1ED9ng ho\u1EB7c mang t\u00EDnh ch\u1EA5t k\u00EDch \u0111\u1ED9ng, b\u1EA1o l\u1EF1c... n\u00F3i chung l\u00E0 tr\u00E1i v\u1EDBi thu\u1EA7n phong m\u1EF9 t\u1EE5c, v\u0103n h\u00F3a v\u00E0 Lu\u1EADt ph\u00E1p Vi\u1EC7t Nam\n- Th\u00F4ng tin t\u1EA1i BkitPOMA lu\u00F4n \u0111\u01B0\u1EE3c c\u1EADp nh\u1EADt.\n- BkitPOMA n\u1EAFm quy\u1EC1n qu\u1EA3n l\u00FD th\u00F4ng tin \u0111\u01B0\u1EE3c xu\u1EA5t b\u1EA3n t\u1EA1i website, c\u00E1c quy\u1EC1n n\u00E0y bao g\u1ED3m: bi\u00EAn t\u1EADp, so\u1EA1n th\u1EA3o, xu\u1EA5t b\u1EA3n, h\u1EE7y b\u1ECF. V\u00E0 BkitPOMA c\u00F3 quy\u1EC1n th\u1EF1c hi\u1EC7n c\u00E1c quy\u1EC1n n\u00E0y m\u00E0 kh\u00F4ng ph\u1EA3i th\u00F4ng b\u00E1o tr\u01B0\u1EDBc.\n\n\t2. Qui \u0111\u1ECBnh khai th\u00E1c th\u00F4ng tin\nNh\u1EEFng \u0111i\u1EC1u sau qui \u0111\u1ECBnh tr\u00E1ch nhi\u1EC7m v\u00E0 quy\u1EC1n l\u1EE3i c\u1EE7a nh\u1EEFng \u0111\u1ED1i t\u01B0\u1EE3ng khai th\u00E1c th\u00F4ng tin t\u1EEB website n\u00E0y.\n- \u0110\u01B0\u1EE3c quy\u1EC1n xem th\u00F4ng tin, gi\u1EDBi thi\u1EC7u cho ng\u01B0\u1EDDi kh\u00E1c xem th\u00F4ng tin t\u1EA1i website.\n- \u0110\u01B0\u1EE3c quy\u1EC1n khai th\u00E1c, s\u1EED d\u1EE5ng t\u1EA5t c\u1EA3 th\u00F4ng tin \u0111\u01B0\u1EE3c xu\u1EA5t b\u1EA3n tr\u00EAn website.\n- \u0110\u01B0\u1EE3c quy\u1EC1n khai th\u00E1c v\u00E0 s\u1EED d\u1EE5ng t\u1EA5t c\u1EA3 h\u00ECnh \u1EA3nh c\u00F3 tr\u00EAn website.\n\nGhi r\u00F5 ngu\u1ED3n BkitPOMA khi th\u1EF1c hi\u1EC7n quy\u1EC1n khai th\u00E1c b\u1EA5t k\u1EF3 th\u00F4ng tin t\u1EEB website.\n- Kh\u00F4ng \u0111\u01B0\u1EE3c quy\u1EC1n s\u1EED d\u1EE5ng nh\u1EEFng h\u00ECnh \u1EA3nh, th\u00F4ng tin t\u1EA1i BkitPOMA \u0111\u1EC3 l\u00E0m ph\u01B0\u01A1ng h\u1EA1i \u0111\u1EBFn h\u00ECnh \u1EA3nh c\u1EE7a BkitPOMA v\u1EDBi c\u1ED9ng \u0111\u1ED3ng.\n- Khi \u0111\u1ED1i t\u01B0\u1EE3ng khai th\u00E1c th\u00F4ng tin t\u1EA1i BkitPOMA vi ph\u1EA1m 1 trong nh\u1EEFng \u0111i\u1EC1u tr\u00EAn, c\u01A1 qu\u1EA3n ch\u1EE7 qu\u1EA3n c\u1EE7a BkitPOMA c\u00F3 quy\u1EC1n s\u1EED d\u1EE5ng nh\u1EEFng bi\u1EC7n ph\u00E1p Ph\u00E1p l\u00FD \u0111\u1EC3 \u0111\u1EA3m b\u1EA3o quy\u1EC1n l\u1EE3i c\u1EE7a BkitPOMA v\u00E0 \u0111\u1ED1i t\u01B0\u1EE3ng khai th\u00E1c th\u00F4ng tin ph\u1EA3i ch\u1ECBu ho\u00E0n to\u00E0n tr\u00E1ch nhi\u1EC7m v\u1EC1 ph\u00E1p l\u00FD v\u1EC1 nh\u1EEFng vi ph\u1EA1m v\u1EDBi nh\u1EEFng qui \u0111\u1ECBnh tr\u00EAn.\n\n\t3. L\u01B0u \u00FD\n- Nh\u1EEFng qui \u0111\u1ECBnh tr\u00EAn \u0111\u01B0\u1EE3c thay \u0111\u1ED5i, \u0111i\u1EC1u ch\u1EC9nh m\u00E0 kh\u00F4ng c\u1EA7n ph\u1EA3i b\u00E1o tr\u01B0\u1EDBc hay th\u00F4ng b\u00E1o.\n- \u0110\u1ED1i t\u01B0\u1EE3ng khai th\u00E1c th\u00F4ng tin c\u00F3 ngh\u0129a v\u1EE5 ph\u1EA3i theo d\u00F5i th\u01B0\u1EDDng xuy\u00EAn nh\u1EEFng qui \u0111\u1ECBnh n\u00E0y tr\u01B0\u1EDBc khi khai th\u00E1c th\u00F4ng tin t\u1EEB website.\n";
	private static final String username = "admin";
	private static final String password = "123456";
	
	public static HashMap<String,String> getHashMapConfig(){
		HashMap<String, String> hashMapConfig = new HashMap<String, String>();
		hashMapConfig.put("title",title);
		hashMapConfig.put("lang", lang);
		hashMapConfig.put("mail",mail);
		hashMapConfig.put("logo", logo);
		hashMapConfig.put("confirm", String.valueOf(confirm));
		hashMapConfig.put("tos",tos);
		hashMapConfig.put("username", username);
		hashMapConfig.put("password", password);
		
		return hashMapConfig;
	}
}
