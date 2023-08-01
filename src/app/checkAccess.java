package app;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class checkAccess {
	//	private static final String ASCII_CHARS = "@B%8WM#*oahkbdpwmZO0QCJYXzcvnxrjft/\\|()1{}[]-_+~<>i!lI;:,\"^`\'. ";
	//	Scanner型インスタンス生成
	private static Scanner sc = new Scanner(System.in);
	//	PrintStream型のオブジェクト初期値設定（メソッドの直接利用が可能）
	private static PrintStream so = System.out;
	//	PrintStream は、ファイルやネットワークなどへの出力にも利用が可能だが、リダイレクトやログ出力などの
	//	機能により出力先が変更されることに注意。また、他者の可読性が低いため利用は推奨されていないようです。
	//	PrintWriter は、高度なエラーハンドリングを行う場合に有用でcheckErrorメソッドでの確認が可能

	//	URL情報確認処理
	public static BufferedImage isUrl(String str) {
		//	TODO URL/URI両判定を行う理由
		try {
			//	URI解析推定
			URI uri = new URI(str);
			//	スキーム（先頭文字列）判定＆戻り値 false ：nullの（http:/https:など含まない）場合はPATHと推定
			if (uri.getScheme() == null) return null;
			//	URLパターン判定
			URL url = new URL(str);
			String pattern = "^(https?|ftp|sftp|scp)://.*$|^data:.*$";
			//	文字列判定＆戻り値 true：パターーマッチする（http:/https:など含む）場合はURLと推定
			if (!str.matches(pattern)) return checkUrl(url);
			//	^(https?|ftp|sftp|scp)://.*$ はhttpまたはs（https）などで始まり、://の
			//	後に任意の文字列が続くURLを判定します。s?はsが0回または1回出現することを表しています。
			//	^data:.*$ はdata:で始まり、その後に任意の文字列が出現することを表しています。
			//	戻り値：checkUrl()によるアクセス状況確認結果
		} catch (URISyntaxException | MalformedURLException e) {
			;
		}
		return null;
	}

	//	URLアクセス状況確認処理
	static BufferedImage checkUrl(URL url){
		try {
			//	HttpURLConnectionによる簡易的通信確認
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			//	URL要求のメソッド指定（リソースの存在や更新日時などのメタデータを取得の為HEADを指定）
			//	GET: リソース取得/読取り, POST: リソース作成/送信/書込み, PUT: リソース作成/更新, DELETE: リソース削除, HEAD: リソースのヘッダ情報取得
			con.setRequestMethod("HEAD");
			//	リモートオブジェクトへの接続開始
			con.connect();
			//	responseCode取得
			//	responseCode: 1xx:情報, 2xx:成功, 3xx:リダイレクション, 4xx:クライアントエラー, 5xx:サーバーエラー
			//	https://docs.oracle.com/javase/jp/8/docs/api/java/net/HttpURLConnection.html
			int status = con.getResponseCode();
			//	リモートオブジェクトへの接続終了
			con.disconnect();
			if (status == HttpURLConnection.HTTP_OK) {
				BufferedImage bi = ImageIO.read(url);
				//	BufferedImage bi = ImageIO.read(new URL(url.toString()));
				return bi;
			} else {
				return null;
			}
		} catch (IOException e) {
			;	//	処理継続	e.printStackTrace();
		}
		return null;
	}

	//	PATH情報確認処理
	public static BufferedImage isPath(String str) {
		try {
			//	パスのパターンを定義
			//	先頭の文字が/又は\で始まることを表しています。
			//	もし先頭が/又は\であればその後に任意の文字列が続くことをを表しています。
			//	もし先頭が/又は\でない場合は、任意の文字列が続くことを表しています。
			//	URLとして別途確認する必要があります。
			//	TODO file型よりも
			File file = new File(str);
			String pathPattern = "^(?:[\\/\\\\]|[^\\/\\\\]).*$";
			if(str.matches(pathPattern)) {
				if(file.exists()) {
				//	if(file.exists() && !file.isDirectory() && checkMime(file)) {
					return checkMime(file);
				}
			} else {
				return null;
			}
		} catch (IOException e) {
			;	//	継続処理	e.printStackTrace();
		}
		return null;
	}

	//	MIME状況確認処理
	static BufferedImage checkMime(File file) throws IOException{
		//	ファイルヘッダーよりContentType取得
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		String mime = URLConnection.guessContentTypeFromStream(bis);
		//	ファイル名より拡張子取得
		String ext = file.getName().toUpperCase();
		so.printf("EXT: \033[42m%5s\033[0m; MIME: \033[42m%10s\033[0m\n",ext.substring(ext.lastIndexOf(".") + 1), mime);
		//	形式判別
		if ((mime == "image/jpeg" && ext.endsWith(".JPG"))
				||  (mime == "image/jpeg" && ext.endsWith(".JEPG"))
				||  (mime == "image/png"  && ext.endsWith(".PNG"))
				||  (mime == "image/gif"  && ext.endsWith(".GIF"))){
//			BufferedImage bi = ImageIO.read(bis);
			BufferedImage bi = ImageIO.read(new File(file.toString()));
			return bi;
		} else {
			so.println("！他のファイルを準備してください。ファイル情報に問題が含まれます。");
			so.printf("EXT: \033[41m%5s\033[0m; MIME: \033[41m%10s\033[0m\n",ext.substring(ext.lastIndexOf(".") + 1), mime);
			return null;
		}
	}
}

/*
【URLConnection.guessContentTypeFromStream】
識別型: ppt:null, xls:null, jpg:image/jpeg, png:image/png, gif:image/gif, txt:null, mov:null, mp4:null, mpg:null
	InputStream is = new BufferedInputStream(new FileInputStream(file));
	return URLConnection.guessContentTypeFromStream(is);

【MimeUtil.registerMimeDetector】精度良
識別型: pdf:application/pdf, ppt:application/msword, xls:application/msword, jpg:image/jpeg, png:image/png, gif:image/gif, txt:application/octet-stream, mov:video/quicktime, mp4:video/mp4, mpg:video/mp2p
	MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
	Collection<?> mime = MimeUtil.getMimeTypes(file);
	if (!mime.isEmpty()) {
		Iterator<?> iterator = mimeTypes.iterator();
		MimeType mime = (MimeType) iterator.next();
		return mime.getMediaType() + "/" + mime.getSubType();
   }
   return "application/octet-stream";
 */

