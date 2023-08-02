package tool;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class GeFileList {
	//	Scanner型インスタンス生成
	private static Scanner sc = new Scanner(System.in);
	//	PrintStream型のオブジェクト初期値設定（メソッドの直接利用が可能）
	private static PrintStream so = System.out;
	//	PrintStream は、ファイルやネットワークなどへの出力にも利用が可能だが、リダイレクトやログ出力などの
	//	機能により出力先が変更されることに注意。また、他者の可読性が低いため利用は推奨されていないようです。
	//	PrintWriter は、高度なエラーハンドリングを行う場合に有用でcheckErrorメソッドでの確認が可能

	public static String getList(String dirPath) {
		//	ストレージ内のファイルリスト表示処理
		File[] list = new File(dirPath).listFiles();
		//	ファイル存在確認＆ファイルリスト表示
		if(list != null) {
			for(int i = 0; i < list.length; i++) {
				if(!list[i].isDirectory()) {
					so.printf("\033[40mNo.%,3d : %s\033[0m\n",i, list[i].getName());
				}
			}
			so.print("\n\033[1m画像ファイル番号を選択してください。\n"
					+"[MENU] ファイル番号選択:\033[34m\033[1m 0～" 
					+(list.length == 0 ? 0 : list.length - 1) 
					+" \033[0m\033[1m その他の場所:\033[34m\033[1m z \033[0m\033[1m"
					+" 終了:\033[34m\033[1m q \033[0m \n\033[1m>>");
		} else {
			File dir = new File(dirPath);
			so.print("\033[1m" + dir + "にファイルが存在しません。"
					+"[MENU] その他の場所:\033[34m z \033[0m\033[1m"
					+" 終了:\033[34m\033[1m q \033[0m\n\033[1m>>");
		}
		
		String ans = sc.nextLine();
		if ((ans.isBlank() || ans == null) || ans.matches("z")) {
			return "z";
		} else if (Integer.parseInt(ans) <= list.length) { 
			System.out.println(list[Integer.parseInt(ans)].getPath());
			return list[Integer.parseInt(ans)].getPath();
			//	終了処理
		} else if (ans.matches("q")) {
			so.println("[処理終了]");
//			sc.close();
//			so.close();
			System.exit(0);
		}
		return "z";
	}

}
