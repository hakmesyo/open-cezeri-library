using System;
using WebSocketSharp;

namespace ClientWebSocket
{
    /*
     * https://github.com/sta/websocket-sharp buradaki nuget paketi kullanılmaktadır, nuget paketini
     * bağımlılıklara eklemek için Araçlar| Nuget paket yöneticisi | Paket Yöneticisi Konsolu geldikten sonra
     * alttaki konsole "Install-Package WebSocketSharp -Pre" komutunu yazıp install etmelisin
     * 
     * 
     * eğer websocketsharp bağımlılığı .net versiyonu ile uyumsuz ise ve warning de belirtiliyorsa
     * https://dotnet.microsoft.com/download/dotnet-framework/net472 dev pack yüklenmeli 
     * ardından csproj dosyasındaki <TargetFramework>net472</TargetFramework> manuel eklenmeli
 <Project Sdk="Microsoft.NET.Sdk">

  <PropertyGroup>
    <OutputType>Exe</OutputType>
    <TargetFramework>net472</TargetFramework>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="WebSocketSharp" Version="1.0.3-rc11" />
  </ItemGroup>

</Project>

     */
    class Program
    {
        private static string clientID;

        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
            using (var ws = new WebSocket("ws://127.0.0.1:8887"))
            {
                ws.Connect();

                ws.OnMessage += (sender, e) =>
                {
                    string str = e.Data;
                    if (str.Contains("[new client connection]"))
                    {
                        Console.WriteLine("connection was accepted, now we send register request to the JIM in order to get ID.. ");
                        ws.Send("consumer says register");
                    }
                    else if (str.Contains("register ID:"))
                    {
                        clientID = str.Split(':')[1];
                        ws.Send("consumer says request inference:" + clientID + ":" + "temp_path");
                    }
                    else if (str.Contains("browser says response:"))
                    {
                        Console.WriteLine(str);
                        ws.Send("consumer says request inference:" + clientID + ":" + @"C:\Dropbox\NetbeansProjects\JIM\models\pistachio_rest\images\test\open\1.jpg");
                        //iterateSender();
                    }
                    //Console.WriteLine("Java server says: " + e.Data);
                };


                ws.Send("merhaba from c#");
                Console.WriteLine("Kapatmak için bir tuşa basın");
                Console.ReadKey(true);
            }
        }
    }
}
