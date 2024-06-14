package com.imeritz.blender.tran;

public class TranConstant {
	public static final byte FD_GS = 0x1F; //GID를 구분하는 구분자로 사용
	public static final byte FD_FS = 0x1E; //필드 사이의 구분자로 사용
	public static final byte FD_RS = 0x1D; //복수의 RECORD가 존재할 때 ROW 구분자로 사용
	public static final byte FD_IS = 0x7F; //FID와 입력 데이터 사이의 구분자로 사용

	public static final byte FID_ARRAY_CODE = '$'; 			//배열 데이터 임을 알려주는 특수문자
	public static final byte[] FID_LIST_SIZE = new byte[8]; 	//데이터길이 8 BYTE (요청: FID리스트길이, 응답: 데이터길이)
	public static final byte[] FID_RECORD_COUNT = new byte[4]; //요구 레코드갯수, 응답 레코드 갯수
	
	public static final String DEFAULT_ROW_COUNT = "40"; 	//기본 요청개수
	
	public static final byte ACTION_KEY_FIRST = '0'; 	//최초 조회
	public static final byte ACTION_KEY_PRIOR = '1';   //이전 데이터 요구
	public static final byte ACTION_KEY_NEXT = '2';    //다음 데이터 요구
	
	//이전/다음 데이터에 대한 상태 플러그
	public static final byte FID_STATUS_DEFAULT = 0x40; //항상 SET (디폴트) @
	public static final byte FID_STATUS_PRIOR = 0x01;   //이전 Enabled
	public static final byte FID_STATUS_NEXT = 0x02;    //다음 Enabled
	public static final byte FID_STATUS_OR = 0x01|0x02; //이전/다음 동시에 존재하는 경우
	
	public static final byte[] SAVE_BUFFER_LEN_DEFAULT = {0x20,0x20,0x20};
		
	public static final int INBLOCK_TYPE_LIST 	   = 1; //블럭에 다중레코드
	public static final int INBLOCK_TYPE_MAP      = 2; //블럭에 단일레코드

	public static final int DATA_LEN_SIZE		   = 8;	
	public static final int ROW_COUNT_SIZE        = 4;
	public static final int ACTION_KEY_SIZE       = 1;
	public static final int STATUS_SIZE           = 1;
	public static final int SAVE_BUFF_LEN_SIZE    = 3;
	
	public static final int DEV_SERVER_A = 1;
	public static final int DEV_SERVER_B = 2;
	public static final int REAL_SERVER_A = 3;
	public static final int REAL_SERVER_B = 4;
	public static final int REAL_SERVER_C = 5;
	public static final int REAL_SERVER_D = 6;
	public static final int TEST_SERVER_A = 7;
	public static final int TEST_SERVER_B = 8;
	public static final int TEST_SERVER_C = 9;
	public static final int TEST_SERVER_D = 10;
	public static final int TEST_SERVER_E = 11;
	public static final int TEST_SERVER_F = 12;
	public static final int TEST_SERVER_G = 13;
	public static final int TEST_SERVER_H = 14;
	public static final int TEST_SERVER_I = 15;
	public static final int TEST_SERVER_J = 16;
	public static final int PBCR_SERVER_A = 17;
}

