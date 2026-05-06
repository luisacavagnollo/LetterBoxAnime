export async function uploadImagem(file) {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("upload_preset", "teste1"); 

  const response = await fetch(
    "https://api.cloudinary.com/v1_1/dwfwwnsu0/image/upload", 
    {
      method: "POST",
      body: formData,
    }
  );

  const data = await response.json();
  return data.secure_url;
}