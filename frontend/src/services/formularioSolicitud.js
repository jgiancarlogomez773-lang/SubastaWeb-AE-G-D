// Arma el multipart/form-data de POST /api/v1/auctions (contratos/sprint-1.md, sección 4).
// Campos: title, description, categoryId, startingPrice, durationHours, image.
export function construirFormulario({ title, description, categoryId, startingPrice, durationHours, image }) {
  const fd = new FormData()
  fd.append('title', title.trim())
  fd.append('description', description.trim())
  fd.append('categoryId', String(categoryId))
  fd.append('startingPrice', String(startingPrice))
  fd.append('durationHours', String(durationHours))
  fd.append('image', image)
  return fd
}
