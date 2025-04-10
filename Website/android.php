 <?php  
           require 'Zugriffesdaten.php';
           $Table=TABLE_NAME;

           $connect = mysqli_connect(DATABASE_HOST, DATABASE_USERNAME, DATABASE_PASSWORD, DATABASE_NAME);  
           $sql = "SELECT * FROM `$Table`";  
           $result = mysqli_query($connect, $sql);  
           $json_array = array();  
           
           while($row = mysqli_fetch_assoc($result))  
           {   

          
                $json_array[] = $row;  
            
           }
           
           $json_array_Reversed = array_reverse($json_array);
           $text='Raum4';
           $klamer1='{';
           $klamer2='}';
           printf('%s ',$klamer1);
           printf('"%s"',$text);
           echo ":";
           echo json_encode($json_array_Reversed);
           printf(' %s',$klamer2);  
           ?>  