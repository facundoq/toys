using Images
using AffineTransforms
using Colors
using ColorTypes
using ColorVectorSpace



function rotate_shear(a::Images.Image,α::Float64)
  α=normalize_angle(α)
  if is_orthogonal_rotation_approx(α)
    return orthogonal_rotation(a,α)
  end

  r=tformrotate(α)
  original_image_size=collect(size(a))
  center=div(original_image_size,2) #center=round(Int,original_image_size/2)
  t(x)=round(Int,r*x)
  new_size,Δ=rotated_image_size(t,original_image_size)
  b=similar(a, new_size)
  edge_size=round(Int,(collect(new_size)-original_image_size)/2)
  println(edge_size)
  println(new_size)
  println(original_image_size)
  d=original_image_size+edge_size-1
  b[:]=Gray(0)
  b[edge_size[1]:d[1],edge_size[2]:d[2]]=a
  fy=-tan(α/2)
  fx=sin(α)
  #dy=round(Int,fy)
  #dx=round(Int,fx)
  shearx(b,fy)
  #sheary(b,dx)
  #shearx(b,dy)
  b
end

function shearx(b::Images.Image,dy::Float64)
  w,h=size(b)
  for i=1:w
    dj,dji=modf(i*dy)
    djint=floor(Int,dji)
    println(djint)
    for j=h:-1:(-djint+1)
      b[i,j]=b[i,j+djint]
    end
    for j=1:(-djint)-1
      b[i,j]=gray(0)
    end
  end
end

function sheary(b::Images.Image,dx::Int)

end

# checks
function is_orthogonal_rotation_approx(α::Float64)
   α=mod(α,0.5*π)
   isapprox(α,0)
end

# function rotl90(a::Images.Image)
#   #rotl180(a,1)
#   d=data(a)
#   d=Base.rotl90(d)
#   copyproperties(a, d)
# end
#
# function rotl90(a::Images.Image,k::Integer)
#   d=data(a)
#   d=Base.rotl90(d,k)
#   copyproperties(a, d)
# end
#
# function rot180(a::Images.Image)
#   #rot180(a,1)
#   d=data(a)
#   d=Base.rot180(d)
#   copyproperties(a, d)
# end
#
# function rot180(a::Images.Image,k::Integer)
#   d=data(a)
#   d=Base.rot180(d,k)
#   copyproperties(a, d)
# end
#
# function rotr90(a::Images.Image)
#   #rotr90(a,1)
#   d=data(a)
#   d=Base.rotr90(d)
#   copyproperties(a, d)
# end
#
# function rotr90(a::Images.Image,k::Integer)
#   d=data(a)
#   d=Base.rotr90(d,k)
#   copyproperties(a, d)
# end

rot180(a::Images.Image)=rot180(a,1)
rotr90(a::Images.Image)=rotr90(a,1)
rotl90(a::Images.Image)=rotl90(a,1)
rot180(a::Images.Image,k::Integer)=copyproperties(a, Base.rot180(data(a),k))
rotr90(a::Images.Image,k::Integer)=copyproperties(a, Base.rotr90(data(a),k))
rotl90(a::Images.Image,k::Integer)=copyproperties(a, Base.rotl90(data(a),k))

function orthogonal_rotation(a::Images.Image,α::Float64)
    if isapprox(α,0)
      return copy(a)
    end
    if isapprox(α,0.5*π)
      return rotl90(a)
    end
    if isapprox(α,π)
      return rot180(a)
    end
    if isapprox(α,1.5*π)
      return rotr90(a)
    end
end

function normalize_angle(α::Float64)
  τ=2*π
  α=mod(α,τ)*sign(α)
  if (α<0)
    α=τ-α
  end
  α
end

function rotate_average(a::Images.Image,α::Float64)

  α=normalize_angle(α)
  if is_orthogonal_rotation_approx(α)
    return orthogonal_rotation(a,α)
  end

  r=tformrotate(α)
  original_image_size=collect(size(a))
  center=div(original_image_size,2) #center=round(Int,original_image_size/2)
  t(x)=round(Int,r*x)
  new_size,Δ=rotated_image_size(t,original_image_size)
  b=similar(a, new_size)
  b[:]=0
  for i=2:original_image_size[1]-1
    for j=2:original_image_size[2]-1
      v=a[i,j]
      new_ij=t([i,j])-Δ+[1 1]'
      ni,nj=tuple(new_ij[:]...)
      #println("$ni-$nj")
      #println("$v -> $(v/5)")
      #v=v/5
      b[ni+1,nj]=v
      #b[ni,nj+1]=v
      #b[ni,nj-1]=v
      #sb[ni-1,nj]=v
      b[ni,nj]  =v
    end
  end
  return b
end

function rotate_simple(a::Images.Image,α::Float64)
  α=normalize_angle(α)
  if is_orthogonal_rotation_approx(α)
    return orthogonal_rotation(a,α)
  end
  r=tformrotate(α)
  original_image_size=collect(size(a))
  center=div(original_image_size,2) #center=round(Int,original_image_size/2)
  t(x)=round(Int,r*x)
  new_size,Δ=rotated_image_size(t,original_image_size)
  b=similar(a, new_size)
  b[:]=Gray(0)
  for i=1:original_image_size[1]
    for j=1:original_image_size[2]
      v=a[i,j]
      new_ij=t([i,j])-Δ+[1 1]'
      ni,nj=tuple(new_ij[:]...)
      b[ni,nj]=v
    end
  end
  return b
end

function inrange(x,y,rx0,ry0,rx1,ry1)
  return rx0 <= x && x <= rx1 && ry0 <= y && y <= ry1
end

function rotate_area_mapping(a::Images.Image,α::Float64)
  α=normalize_angle(α)
  if is_orthogonal_rotation_approx(α)
    return orthogonal_rotation(a,α)
  end
  r=tformrotate(α)
  original_image_size=collect(size(a))
  center=div(original_image_size,2) #center=round(Int,original_image_size/2)
  t(x)=round(Int,r*x)
  rinv=tformrotate(-α)
  tinv(x)=round(Int,rinv*x)
  new_size,Δ=rotated_image_size(t,original_image_size)
  b=similar(a, new_size)
  b[:]=Gray(0)
  Δ=vec(Δ)
  c=0.5
  c2=0.5/8
  convolution_matrix=[c2 c2 c2
                      c2 c  c2
                      c2 c2 c2]
  for i=2:new_size[1]-1
    for j=2:new_size[2]-1
      original_xy=tinv([i,j]+Δ)
      ni,nj=tuple(original_xy[:]...)
      if inrange(ni,nj,2,2,original_image_size[1]-1,original_image_size[2]-1)
        #b[i,j]=(a[ni+1,nj]+a[ni,nj+1]+a[ni,nj-1]+a[ni-1,nj]+a[ni,nj])
        #b[i,j]=a[ni,nj]
        #b[i,j]=a[ni,nj]/2+a[ni+1,nj]/2
        #b[i,j]=c2*a[ni+1,nj]+c2*a[ni,nj+1]+c2*a[ni,nj-1]+c2*a[ni-1,nj]+c*a[ni,nj]
        b[i,j]= sum(a[ni-1:ni+1,nj-1:nj+1].*convolution_matrix)
      end
    end
  end
  return b

end

function rotated_image_size(t::Function,original_image_size::Vector{Int64})
  vertices=[t([1,1]) t([1,original_image_size[2]]) t([original_image_size[1],1]) t(original_image_size)]
  vertices=round(Int,vertices)
  x_vertices=vertices[1,:]
  y_vertices=vertices[2,:]

  Δ=[minimum(x_vertices) minimum(y_vertices)]
  new_size= [maximum(x_vertices) maximum(y_vertices)]-Δ+[1 1]
  new_size=tuple(new_size[:]...)
  return new_size,Δ'
end

image_name="camera"
#image_name="lena"
a=Images.load("$image_name.png")
αs_deg=[30]
αs_deg=[0,15,30,45,60,75,105,120,135]
#αs_deg=[30,45,60,75,135]
#αs_deg=[0,90,180,270]

convert(RGB, Gray(0.0))

αs=αs_deg/360*(2*pi)
mkpath(image_name)
for i=1:length(αs)
  α=αs[i]
  println("Rotating by $(αs_deg[i])")
  b=rotate_area_mapping(a,α)
  save("$(image_name)/rotated_$(αs_deg[i]).png",b)
end
