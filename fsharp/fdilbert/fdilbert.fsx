#light
module net = System.Net
module io = System.IO
module text = System.Text
module regex = System.Text.RegularExpressions
module thread = System.Threading

type ProxyServer = { server : string ; port : string }
type Credentials = { login_name : string ; password : string }

let create_proxy (proxy : ProxyServer) = new net.WebProxy(proxy.server + ":" + proxy.port)
let create_credentials (cred : Credentials) = new net.NetworkCredential(cred.login_name, cred.password)

type ProxyWithCredentialsData = { proxy : ProxyServer; credentials : Credentials }

type Authentication = 
    | ProxyWithCredentials of ProxyWithCredentialsData
    | Proxy of ProxyServer
    | Direct

let seconds (count : int) = new System.TimeSpan(0, 0, count) 

let date_as_string (date : System.DateTime) (format : string) = 
    let date_format = (new System.Globalization.CultureInfo( "en-US", false )).DateTimeFormat
    date.ToString(format, date_format)

let with_open_connection_do (request : net.HttpWebRequest) (f : io.Stream -> 'unit) = 
    request.KeepAlive <- false
    using (request.GetResponse() :?> net.HttpWebResponse) 
        (fun response ->
            if response.StatusCode = net.HttpStatusCode.OK 
                then using (response.GetResponseStream()) f 
                else failwith "HttpWebResponse is not ok"
        )

let create_request (url : string) = printfn "? %s" url ; net.HttpWebRequest.Create(url) :?> net.HttpWebRequest
let create_request_with_authentication (auth : Authentication) = (fun url -> 
     let request = create_request url
     match auth with
        Authentication.Direct -> request
        | Authentication.Proxy x -> request.Proxy <- create_proxy x ; request
        | Authentication.ProxyWithCredentials (data) ->
            request.Proxy <- create_proxy data.proxy
            request.Proxy.Credentials <- create_credentials data.credentials
            request      
    )
    
let picture_suffix (date : System.DateTime) =
     match date.DayOfWeek with
     System.DayOfWeek.Sunday -> ".jpg"
     | _ -> ".gif"

let picture_file_name (date : System.DateTime) (archive_path : string) = archive_path + (date_as_string date "yyyy_MM_dd") + picture_suffix(date)

let picture_exists (date : System.DateTime) (archive_path : string) = io.File.Exists (picture_file_name date archive_path)
    
let dilbert_url (date : System.DateTime) = string.Format("http://www.dilbert.com/comics/dilbert/archive/dilbert-{0}.html", (date_as_string date "yyyyMMdd"))

let dilbert_web_page_content (date : System.DateTime) (create_request : string -> net.HttpWebRequest) =
    let all_content (stream: io.Stream) = 
        let encoding = text.Encoding.GetEncoding("utf-8")
        using (new io.StreamReader(stream)) (fun stream_reader -> stream_reader.ReadToEnd())
    with_open_connection_do (date |> dilbert_url |> create_request) all_content


let dilbert_picture_url (date : System.DateTime) (web_page_content : string) =
    let search = new regex.Regex(@"Attachments=/comics/dilbert/archive/images/(dilbert\d+\.(gif|jpg))")
    let matching = search.Match(web_page_content)
    if matching.Groups.Count > 1 
        then string.Format("http://www.dilbert.com/comics/dilbert/archive/images/{0}", matching.Groups.Item(1).Value) 
        else failwith "no dilbert picture url was found parsing the web page content"


let save_content_to_file (input: io.Stream) (file_name : string) = 
    using (new io.FileStream(file_name, io.FileMode.CreateNew, io.FileAccess.Write)) 
           (fun output_file -> 
                using (new io.BinaryWriter(output_file)) 
                    (fun writer -> 
                        let buffer = Array.create 1024 (Byte.of_int 0)
                        let bytes_read = ref 0
                        while (bytes_read := input.Read(buffer, 0, buffer.Length); !bytes_read > 0) do 
                            writer.Write (buffer, 0, !bytes_read)
                    )
        )

let save_dilbert_strip (date : System.DateTime) (archive_path : string) = fun (input : io.Stream) -> save_content_to_file  input (picture_file_name date archive_path)

let get_dilbert_strip (date : System.DateTime) (archive_path : string) (create_request : string -> net.HttpWebRequest) =
    let download_strip = (fun (date : System.DateTime) (archive_path : string) (create_request : string -> net.HttpWebRequest) ->
        let content = dilbert_web_page_content date create_request
        let url = dilbert_picture_url date content
        let request = create_request url
        with_open_connection_do request (save_dilbert_strip date archive_path)
        printfn "+ %s" (date_as_string date "yyyy-MM-dd")
    )    
    if picture_exists date archive_path 
        then printfn "= %s" (date_as_string date "yyyy-MM-dd")
        else download_strip date archive_path create_request

let auth = Authentication.Direct

let my_get_dilbert (date : System.DateTime) = get_dilbert_strip date  @"X:\backup\dilbert_archive\" (create_request_with_authentication auth)

let previous_days (count : int) (date : System.DateTime)  = { for x in 0 .. -1 .. -count -> date.AddDays(Float.of_int x) }

let sync_get_dilbert_for_last_days (days : int) = (previous_days days System.DateTime.Today ) |> Seq.iter my_get_dilbert

type CountdownLatch = class
    val remaining : int ref
    val event : thread.ManualResetEvent
    new (count) = { remaining = count ; event = new thread.ManualResetEvent(false) }
    member x.Decrement() = lock(x) (fun() -> x.remaining := !x.remaining - 1 ; !x.remaining)
    member x.Signal() = if (x.Decrement() = 0) then x.event.Set() |> ignore
    member x.Wait() = x.event.WaitOne() |> ignore
end

let async_get_dilbert_for_last_days (days : int) =
    let latch = new CountdownLatch(ref days)
    (previous_days days System.DateTime.Today ) |> Seq.iter (fun date -> 
        thread.ThreadPool.QueueUserWorkItem (fun (state) -> my_get_dilbert date; latch.Signal ()) |> ignore) 
    latch.Wait ()

//async_get_dilbert_for_last_days 30
sync_get_dilbert_for_last_days 30