package util;


public class ColorChart {

	public static void main(String[] args) {
		//	AnsiConsole.systemInstall(); // Jansiを初期化

		//	256色チャート（コード記述表記・縦並版）
		System.out.println("256 COLOR CHART");
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				int v = i*16+j;
				//	\e[38;5;nm	nを0～255のカラーコードを指定（\e: \033）（%02X： %xで整数を十六進数で出力し、足りない桁数(2桁)を0埋め）
				System.out.printf("\033[38;5;%dm%02X \\033[38;5;%dm  	\033[0m", v, v, v);
				//	System.out.printf("\033[38;5;%dm%02X \033[0m ", v, v);
				//	System.out.printf("\033[38;5;%dm%02X \\033[38;5;%dm \033[0m ", v, v, v);
				System.out.printf("\033[48;5;%dm%02X \\033[48;5;%dm  	\033[0m", v, v, v);
				//	System.out.printf("\033[48;5;%dm%02X \033[0m ", v, v);
				//	System.out.printf("\033[48;5;%dm%02X \\033[48;5;%dm \033[0m ", v, v, v);
				System.out.printf("\033[0m\n");
			}
			System.out.printf("\033[0m");
		}


		//	256色チャート（16進数版）
		System.out.println("\n256 COLOR CHART");
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				int v = i*16+j;
				//	\e[38;5;nm	nを0～255のカラーコードを指定（\e: \033）（%02X： %xで整数を十六進数で出力し、足りない桁数(2桁)を0埋め）
				System.out.printf("\033[38;5;%dm %02X \033[0m", v, v);
				System.out.printf("\033[48;5;%dm %02X \033[0m", v, v);
				System.out.printf("\033[0m");
			}
			System.out.printf("\033[0m\n");
		}

		//	8色チャート（コード記述表記版）
		System.out.println("\n8 COLOR CHART");
		for(int i = 0; i < 2 ; i++){
			for(int j = 0; j < 4 ; j++){
				int v = i*4+j;
				System.out.printf("\u001b[00;3%dm \\u001B[00;3%dm \u001b[0m", v, v);
				System.out.printf("\u001b[00;4%dm \\u001B[00;4%dm \u001b[0m", v, v);   
				//	System.out.printf("\u001b[00;4"+i+"m esc[4"+i+" \u001b[00m");
			}
			System.out.printf("\u001b[0m\n");
		}
		System.out.printf("\u001b[0m\n");

		System.out.println("\n8 COLOR CHART");
		for(int i = 0; i < 2 ; i++){
			for(int j = 0; j < 4 ; j++){
				int v = i*4+j;
				System.out.printf("\033[3%dm \\033[3%dm \033[0m", v, v);
				System.out.printf("\033[4%dm \\033[4%dm \033[0m", v, v);   
				//	System.out.printf("\0033[00;4"+i+"m esc[4"+i+" \0033[00m");
			}
			System.out.printf("\033[0m\n");
		}
		System.out.printf("\033[0m\n");

	}
}
/*
出力文字の変更
注：ターミナルによって対応してる/してないが分かれる
記法	内容
\e[0m	指定をリセットし未指定状態に戻す（0は省略可）
\e[1m	太字
\e[2m	薄く表示
\e[3m	イタリック
\e[4m	アンダーライン
\e[5m	ブリンク
\e[6m	高速ブリンク
\e[7m	文字色と背景色の反転
\e[8m	表示を隠す（コピペ可能）
\e[9m	取り消し

出力色の変更
先述した \e[0m でリセット可能。
文字色	背景色	色
\e[30m	\e[40m	黒
\e[31m	\e[41m	赤
\e[32m	\e[42m	緑
\e[33m	\e[43m	黄色
\e[34m	\e[44m	青
\e[35m	\e[45m	マゼンダ
\e[36m	\e[46m	シアン
\e[37m	\e[47m	白
出力色の変更（拡張）
注：ターミナルによって対応してる/してないが分かれる

文字色			背景色			内容
\e[38;5;nm		\e[48;5;nm		nを0～255のカラーコードを指定
\e[38;2;r;g;bm	\e[48;2;r;g;bm	RGBでのカラーコードを指定
								（対応端末少ない）
*/