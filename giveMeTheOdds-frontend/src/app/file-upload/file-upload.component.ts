import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ɵdevModeEqual } from '@angular/core';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  fileName : String = ""
  file!: File
  isError : boolean = false
  error : string 
  uploaded : boolean = false
  gotResults : boolean = false
  probability : number
  day : number
  planet : string
  planetList : string[]

  constructor(private httpClient : HttpClient) {

   }

  ngOnInit(): void {
    
  }


  onFileSelected(event){
    this.file = event.target.files[0]
    this.fileName = this.file.name
    this.isError = false
  }

  upload(){

    if(this.file == null){

      this.error = "No file found, make sure to select a file first"
      this.isError = true
      return 
    } 
    this.isError = false
    this.httpClient.post("http://localhost:4567/upload", this.file).subscribe((data) => {
    this.uploaded = true
    document.getElementById("chosen-file").style.visibility="hidden"
    document.getElementById("uploaded-file").style.visibility="visible"
    console.log(data)
    })
  }

  getBestItinerary(){
    if(!this.uploaded){
      this.error = "No file was uploaded to the server, make sure you upload one before trying to compute probability"
      this.isError = true
      return
    }
    this.httpClient.get("http://localhost:4567/probability").subscribe((data) => {
    console.log(data)
    Object.keys(data).map((key) => {
      if(key == "probability"){
        this.probability = data[key]
      } else if (key == "day"){
        this.day = data[key]
      } else if (key == "destination"){
        this.planet = data[key]
      } else if (key == "planetList"){
        this.planetList = data[key]
      }
    })
    this.gotResults = true
    })
  }




}
